package services;

import event.payment.history.PaymentHistoryReplied;
import event.payment.history.PaymentHistoryExtendedReplied;
import event.payment.pay.PayReplied;
import event.payment.pay.PayRequested;
import event.payment.history.PaymentHistoryRequested;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.PaymentHistory;
import rest.PaymentRequest;
import sharedMisc.QueueUtils;
import team14messaging.ReplyWaiter;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PaymentService {

    private final MessageQueue queue = new RabbitMqQueue(QueueUtils.getQueueName());
    private final ReplyWaiter waiter = new ReplyWaiter(queue,
            PaymentHistoryReplied.topic, PaymentHistoryExtendedReplied.topic,
            PayReplied.topic
    );

    public PaymentService() {}

    public String pay(PaymentRequest payment) {
        final String correlationId = UUID.randomUUID().toString();

        waiter.registerWaiterForCorrelation(correlationId);

        queue.publish(new Event(PayRequested.topic, new Object[]{
                new PayRequested(
                        correlationId,
                        payment.getTokenId(),
                        payment.getMerchantId(),
                        payment.getAmount(),
                        payment.getDescription()
                )}));

        var event = waiter.synchronouslyWaitForReply(
                correlationId
        );

        var reply = event.getArgument(0, PayReplied.class);

        if (reply.getSuccessResponse() != null) {
            return reply.getSuccessResponse().getId();
        } else {
            // TODO: Throw, so that the status code is 400
            return reply.getFailResponse().getMessage();
        }
    }

    public List<PaymentHistoryReplied> paymentHistory(PaymentHistory user) {
        final String correlationId = UUID.randomUUID().toString();
        queue.publish(new Event(PaymentHistoryRequested.topic, new Object[]{
            new PaymentHistoryRequested(
                    correlationId,
                    user.getUserId(),
                    user.getUserType()
            )
        }));
        var event = waiter.synchronouslyWaitForReply(
            correlationId
        );
        PaymentHistoryExtendedReplied historyListExtended = event.getArgument(0, PaymentHistoryExtendedReplied.class);
        return historyListExtended.getHistoryList();
    }

}