package dk.dtu.team14.services;

import dk.dtu.team14.data.Payment;
import dk.dtu.team14.db.PaymentHistory;
import event.account.BankAccountIdFromCustomerIdReplied;
import event.account.BankAccountIdFromMerchantIdReplied;
import event.account.BankAccountIdFromMerchantIdRequested;
import event.payment.history.*;
import event.payment.pay.PayReplied;
import event.payment.pay.PayRequested;
import event.token.CustomerIdFromTokenRequested;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.PaymentHistoryCustomer;
import rest.PaymentHistoryManager;
import rest.PaymentHistoryMerchant;
import sharedMisc.QueueUtils;
import team14messaging.ReplyWaiter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PaymentService {
    private final MessageQueue queue;
    private final ReplyWaiter waiter;

    public PaymentService(MessageQueue mq) {
        queue = mq;
        waiter = new ReplyWaiter(
                queue,
                BankAccountIdFromCustomerIdReplied.topic,
                BankAccountIdFromMerchantIdReplied.topic
        );
    }

    private final PaymentHistory paymentHistory = new PaymentHistory();
    private final BankService bank = new BankServiceService().getBankServicePort();

    private Executor executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        System.out.println("Payment service running...");
        var mq = new RabbitMqQueue(QueueUtils.getQueueName(args[0]));
        PaymentService paymentService = new PaymentService(mq);
        paymentService.handleIncomingMessages();
    }

    public void handleIncomingMessages() {
        queue.addHandler(PayRequested.topic, this::handlePayRequest);
        queue.addHandler(PaymentHistoryRequested.PaymentCustomerHistoryRequested.topic, this::handlePaymentCustomerHistoryRequest);
        queue.addHandler(PaymentHistoryRequested.PaymentMerchantHistoryRequested.topic, this::handlePaymentMerchantHistoryRequest);
        queue.addHandler(PaymentHistoryRequested.PaymentManagerHistoryRequested.topic, this::handlePaymentManagerHistoryRequest);
    }

    public void handlePayRequest(Event event) {
        final var payRequest = event.getArgument(0, PayRequested.class);
        System.out.println("Handling pay request - " + payRequest.getCorrelationId());

        // Get the customer id. We will sent a request to token service, which
        // will sent a request to account service and reply with both customer id
        // and bankAccountId. We also need to request merchant bank account id
        // from the account service

        final UUID merchantBankAccountRequestCorrelationId = UUID.randomUUID();
        final UUID customerIdAndBankAccountFromTokenCorrelationId = UUID.randomUUID();

        waiter.registerWaiterForCorrelation(merchantBankAccountRequestCorrelationId);
        waiter.registerWaiterForCorrelation(customerIdAndBankAccountFromTokenCorrelationId);

        queue.publish(new Event(
                BankAccountIdFromMerchantIdRequested.topic,
                new Object[]{
                        new BankAccountIdFromMerchantIdRequested(
                                merchantBankAccountRequestCorrelationId,
                                payRequest.getMerchantId()
                        )
                }
        ));

        queue.publish(
                new Event(
                        CustomerIdFromTokenRequested.topic,
                        new Object[]{
                                new CustomerIdFromTokenRequested(
                                        customerIdAndBankAccountFromTokenCorrelationId,
                                        payRequest.getTokenId()
                                )
                        }
                )
        );

        var merchantBankAccountIdResponse =
                waiter.synchronouslyWaitForReply(merchantBankAccountRequestCorrelationId);
        var customerIdAndBankAccountFromTokenIdResponse =
                waiter.synchronouslyWaitForReply(customerIdAndBankAccountFromTokenCorrelationId);
        final BankAccountIdFromMerchantIdReplied merchantBankAccount =
                merchantBankAccountIdResponse.getArgument(0, BankAccountIdFromMerchantIdReplied.class);
        final BankAccountIdFromCustomerIdReplied customerBankAccountAndId =
                customerIdAndBankAccountFromTokenIdResponse.getArgument(0, BankAccountIdFromCustomerIdReplied.class);
        try {
            bank.transferMoneyFromTo(
                    customerBankAccountAndId.getSuccessResponse().getBankAccountId(),
                    merchantBankAccount.getSuccessResponse().getBankAccountId(),
                    payRequest.getAmount(),
                    payRequest.getDescription()
            );
        } catch (BankServiceException_Exception e) {
            publishErrorDuringPayment(
                    payRequest.getCorrelationId(),
                    e.getMessage());
            return;
        }

        paymentHistory.addPaymentHistory(UUID.randomUUID(), new Payment(customerBankAccountAndId.getSuccessResponse().getCustomerId(), payRequest.getMerchantId(), payRequest.getAmount(), payRequest.getDescription(), new Timestamp(System.currentTimeMillis()), customerBankAccountAndId.getSuccessResponse().getCustomerName(), merchantBankAccount.getSuccessResponse().getMerchantName()));

        var replyEvent = new PayReplied(
                payRequest.getCorrelationId(),
                new PayReplied.PayRepliedSuccess(
                        "0f5de96a-c50b-4010-bbfa-5f5d8e1af693", //TODO: un-hardcode paymentId
                        payRequest.getAmount(),
                        payRequest.getDescription()
                )
        );

        queue.publish(new Event(
                PayReplied.topic,
                new Object[]{replyEvent}
        ));

    }

    private void handlePaymentCustomerHistoryRequest(Event event) {
        final var paymentCustomerHistoryRequest = event.getArgument(0, PaymentHistoryRequested.PaymentCustomerHistoryRequested.class);
        System.out.println("Handling payment history request user - " + paymentCustomerHistoryRequest.getCustomerId());
        List<PaymentHistoryCustomer> customerHistoryList = paymentHistory.getCustomerHistory(paymentCustomerHistoryRequest.getCustomerId());
        var replyEvent = new PaymentHistoryReplied.PaymentCustomerHistoryReplied(
                paymentCustomerHistoryRequest.getCorrelationId(),
                customerHistoryList
        );
        queue.publish(new Event(
                PaymentHistoryReplied.PaymentCustomerHistoryReplied.topic,
                new Object[]{replyEvent}
        ));
    }

    private void handlePaymentMerchantHistoryRequest(Event event) {
        final var paymentMerchantHistoryRequest = event.getArgument(0, PaymentHistoryRequested.PaymentMerchantHistoryRequested.class);
        System.out.println("Handling payment history request user - " + paymentMerchantHistoryRequest.getMerchantId());
        List<PaymentHistoryMerchant> merchantHistoryList = paymentHistory.getMerchantHistory(paymentMerchantHistoryRequest.getMerchantId());
        var replyEvent = new PaymentHistoryReplied.PaymentMerchantHistoryReplied(
                paymentMerchantHistoryRequest.getCorrelationId(),
                merchantHistoryList
        );
        queue.publish(new Event(
                PaymentHistoryReplied.PaymentMerchantHistoryReplied.topic,
                new Object[]{replyEvent}
        ));
    }

    private void handlePaymentManagerHistoryRequest(Event event) {
        final var paymentManagerHistoryRequest = event.getArgument(0, PaymentHistoryRequested.PaymentManagerHistoryRequested.class);
        System.out.println("Handling payment history request user - manager");
        List<PaymentHistoryManager> managerHistoryList = paymentHistory.getManagerHistory();
        var replyEvent = new PaymentHistoryReplied.PaymentManagerHistoryReplied(
                paymentManagerHistoryRequest.getCorrelationId(),
                managerHistoryList
        );
        queue.publish(new Event(
                PaymentHistoryReplied.PaymentManagerHistoryReplied.topic,
                new Object[]{replyEvent}
        ));
    }

    private void publishErrorDuringPayment(UUID correlationId, String message) {
        queue.publish(new Event(
                PayReplied.topic,
                new Object[]{new PayReplied(
                        correlationId,
                        new PayReplied.PayRepliedFailure(message)
                )}
        ));
    }

}
