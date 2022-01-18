package services;

import event.payment.history.*;
import rest.PaymentHistoryCustomer;
import event.payment.pay.PayReplied;
import event.payment.pay.PayRequested;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.PaymentHistoryManager;
import rest.PaymentHistoryMerchant;
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
            PaymentManagerHistoryReplied.topic,
            PaymentCustomerHistoryReplied.topic,
            PaymentMerchantHistoryReplied.topic,
            PaymentCustomerHistoryReplied.topic,
            PayReplied.topic
    );

    public PaymentService() {}

    public String pay(PaymentRequest payment) {
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

        if (reply.getSuccessResponse() != null) {
            return reply.getSuccessResponse().getId();
        } else {
            // TODO: Throw, so that the status code is 400
            return reply.getFailResponse().getMessage();
        }
    }

    public List<PaymentHistoryCustomer> customerPaymentHistory(UUID customerId) {
        final UUID correlationId = UUID.randomUUID();
        waiter.registerWaiterForCorrelation(correlationId);
        queue.publish(new Event(PaymentCustomerHistoryRequested.topic, new Object[]{
                new PaymentCustomerHistoryRequested(
                        correlationId,
                        customerId
                )
        }));
        var event = waiter.synchronouslyWaitForReply(
                correlationId
        );
        PaymentCustomerHistoryReplied customerHistoryList = event.getArgument(0, PaymentCustomerHistoryReplied.class);
        return customerHistoryList.getCustomerHistoryList();
    }

    public List<PaymentHistoryMerchant> merchantPaymentHistory(UUID merchantId) {
        final UUID correlationId = UUID.randomUUID();
        waiter.registerWaiterForCorrelation(correlationId);
        queue.publish(new Event(PaymentMerchantHistoryRequested.topic, new Object[]{
                new PaymentMerchantHistoryRequested(
                        correlationId,
                        merchantId
                )
        }));
        var event = waiter.synchronouslyWaitForReply(
                correlationId
        );
        PaymentMerchantHistoryReplied merchantHistoryList = event.getArgument(0, PaymentMerchantHistoryReplied.class);
        return merchantHistoryList.getMerchantHistoryList();
    }

    public List<PaymentHistoryManager> managerPaymentHistory() {
        final UUID correlationId = UUID.randomUUID();
        waiter.registerWaiterForCorrelation(correlationId);
        queue.publish(new Event(PaymentManagerHistoryRequested.topic, new Object[]{
                new PaymentManagerHistoryRequested(
                        correlationId
                )
        }));
        var event = waiter.synchronouslyWaitForReply(
                correlationId
        );
        PaymentManagerHistoryReplied managerHistoryList = event.getArgument(0, PaymentManagerHistoryReplied.class);
        return managerHistoryList.getManagerHistoryList();
    }

}