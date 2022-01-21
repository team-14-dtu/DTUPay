package services;

import event.payment.history.PaymentHistoryReplied;
import event.payment.history.PaymentHistoryRequested;
import event.payment.pay.PayReplied;
import event.payment.pay.PayRequested;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.PaymentRequested;
import sharedMisc.QueueUtils;
import team14messaging.ReplyWaiter;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class PaymentService {

    private final MessageQueue queue = new RabbitMqQueue(QueueUtils.getQueueName());
    private final ReplyWaiter waiter = new ReplyWaiter(queue,
            PaymentHistoryReplied.PaymentManagerHistoryReplied.topic,
            PaymentHistoryReplied.PaymentCustomerHistoryReplied.topic,
            PaymentHistoryReplied.PaymentMerchantHistoryReplied.topic,
            PayReplied.topic
    );

    public PaymentService() {
    }

    // @author : David
    public PayReplied pay(PaymentRequested payment) {
        final UUID correlationId = UUID.randomUUID();

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

        return reply;
    }
    // @author : Søren
    public PaymentHistoryReplied.PaymentCustomerHistoryReplied customerPaymentHistory(UUID customerId) {
        final UUID correlationId = UUID.randomUUID();
        waiter.registerWaiterForCorrelation(correlationId);
        queue.publish(new Event(PaymentHistoryRequested.PaymentCustomerHistoryRequested.topic, new Object[]{
                new PaymentHistoryRequested.PaymentCustomerHistoryRequested(
                        correlationId,
                        customerId
                )
        }));
        var event = waiter.synchronouslyWaitForReply(
                correlationId
        );
        PaymentHistoryReplied.PaymentCustomerHistoryReplied customerHistoryList = event.getArgument(0, PaymentHistoryReplied.PaymentCustomerHistoryReplied.class);
        return customerHistoryList;
    }
    // @author : David
    public PaymentHistoryReplied.PaymentMerchantHistoryReplied merchantPaymentHistory(UUID merchantId) {
        final UUID correlationId = UUID.randomUUID();
        waiter.registerWaiterForCorrelation(correlationId);
        queue.publish(new Event(PaymentHistoryRequested.PaymentMerchantHistoryRequested.topic, new Object[]{
                new PaymentHistoryRequested.PaymentMerchantHistoryRequested(
                        correlationId,
                        merchantId
                )
        }));
        var event = waiter.synchronouslyWaitForReply(
                correlationId
        );
        PaymentHistoryReplied.PaymentMerchantHistoryReplied merchantHistoryList = event.getArgument(0, PaymentHistoryReplied.PaymentMerchantHistoryReplied.class);
        return merchantHistoryList;
    }
    // @author : Søren
    public PaymentHistoryReplied.PaymentManagerHistoryReplied managerPaymentHistory() {
        final UUID correlationId = UUID.randomUUID();
        waiter.registerWaiterForCorrelation(correlationId);
        queue.publish(new Event(PaymentHistoryRequested.PaymentManagerHistoryRequested.topic, new Object[]{
                new PaymentHistoryRequested.PaymentManagerHistoryRequested(
                        correlationId
                )
        }));
        var event = waiter.synchronouslyWaitForReply(
                correlationId
        );
        PaymentHistoryReplied.PaymentManagerHistoryReplied managerHistoryList = event.getArgument(0, PaymentHistoryReplied.PaymentManagerHistoryReplied.class);
        return managerHistoryList;
    }

}