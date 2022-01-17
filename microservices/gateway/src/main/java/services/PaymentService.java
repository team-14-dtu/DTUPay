package services;

import event.payment.history.ReplyPaymentHistory;
import event.payment.history.ReplyPaymentHistoryExtended;
import event.payment.pay.ReplyPay;
import event.payment.pay.RequestPay;
import event.payment.history.RequestPaymentHistory;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.Payment;
import rest.PaymentHistory;
import sharedMisc.QueueUtils;
import team14messaging.ReplyWaiter;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PaymentService {

    private final MessageQueue queue = new RabbitMqQueue(QueueUtils.getQueueName());
    private final ReplyWaiter waiter = new ReplyWaiter(queue,
            ReplyPaymentHistory.topic, ReplyPaymentHistoryExtended.topic,
            ReplyPay.topic
    );

    public PaymentService() {}

    public String pay(Payment payment) {
        final String correlationId = UUID.randomUUID().toString();

        queue.publish(new Event(RequestPay.topic, new Object[]{
                new RequestPay(
                        correlationId,
                        payment.getId(),
                        payment.getDebtorId(),
                        payment.getCreditorId(),
                        payment.getAmount(),
                        payment.getDescription()
                )}));

        var event = waiter.synchronouslyWaitForReply(
                correlationId
        );

        var reply = event.getArgument(0, ReplyPay.class);

        if (reply.getSuccessResponse() != null) {
            return reply.getSuccessResponse().getId();
        } else {
            return reply.getFailResponse().getMessage();
        }
    }

    public List<ReplyPaymentHistory> paymentHistory(PaymentHistory user) {
        final String correlationId = UUID.randomUUID().toString();
        queue.publish(new Event(RequestPaymentHistory.topic, new Object[]{
            new RequestPaymentHistory(
                    correlationId,
                    user.getUserId(),
                    user.getUserType()
            )
        }));
        var event = waiter.synchronouslyWaitForReply(
            correlationId
        );
        ReplyPaymentHistoryExtended historyListExtended = event.getArgument(0, ReplyPaymentHistoryExtended.class);
        return historyListExtended.getHistoryList();
    }

}