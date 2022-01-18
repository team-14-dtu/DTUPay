package dk.dtu.team14.services;

import dk.dtu.team14.db.PaymentHistory;
import event.account.BankAccountIdFromCustomerIdReplied;
import event.account.BankAccountIdFromMerchantIdReplied;
import event.account.BankAccountIdFromMerchantIdRequested;
import event.payment.history.PaymentHistoryRequested;
import event.payment.pay.PayReplied;
import event.payment.pay.PayRepliedFailure;
import event.payment.pay.PayRepliedSuccess;
import event.payment.pay.PayRequested;
import event.token.CustomerIdFromTokenRequested;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import sharedMisc.QueueUtils;
import team14messaging.ReplyWaiter;

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


    /*private final ReplyWaiter waiter = new ReplyWaiter(
            queue,
            ReplyBankAccountIdFromCustomerId.topic,
            ReplyBankAccountIdFromMerchantId.topic
    );*/
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
        queue.addHandler(PaymentHistoryRequested.topic, this::handlePaymentHistoryRequest);
        queue.addHandler(PayRequested.topic, this::handlePayRequest);
    }

    public void handlePayRequest(Event event) {
        executor.execute(() -> {
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

            System.out.println("here0, waiting for:" + merchantBankAccountRequestCorrelationId);

            var merchantBankAccountIdResponse =
                    waiter.synchronouslyWaitForReply(merchantBankAccountRequestCorrelationId);
            System.out.println("here0.5");

            var customerIdAndBankAccountFromTokenIdResponse =
                    waiter.synchronouslyWaitForReply(customerIdAndBankAccountFromTokenCorrelationId);

            System.out.println("here1");
            final BankAccountIdFromMerchantIdReplied merchantBankAccount =
                    merchantBankAccountIdResponse.getArgument(0, BankAccountIdFromMerchantIdReplied.class);
            System.out.println("here2");
            final BankAccountIdFromCustomerIdReplied customerBankAccountAndId =
                    customerIdAndBankAccountFromTokenIdResponse.getArgument(0, BankAccountIdFromCustomerIdReplied.class);
            System.out.println("here3");
        try {
            bank.transferMoneyFromTo(
                    customerBankAccountAndId.getBankAccountId(),
                    merchantBankAccount.getBankAccountId(),
                    payRequest.getAmount(),
                    payRequest.getDescription()
            );
        } catch (BankServiceException_Exception e) {
            publishErrorDuringPayment(
                    payRequest.getCorrelationId(),
                    "Bankservice payment error");
            return;
        }

//        paymentHistory.addPaymentHistory(new Payment(payRequest.getId(), payRequest.getMerchantId(), payRequest.getTokenId(), payRequest.getAmount(), payRequest.getDescription()));

            var replyEvent = new PayReplied(
                    payRequest.getCorrelationId(),
                    new PayRepliedSuccess(
                            "0f5de96a-c50b-4010-bbfa-5f5d8e1af693",
                            payRequest.getAmount(),
                            payRequest.getDescription()
                    ),
                    null
            );

            queue.publish(new Event(
                    PayReplied.topic,
                    new Object[]{replyEvent}
            ));
        });
    }

    public void handlePaymentHistoryRequest(Event event) {
//        final var paymentHistoryRequest = event.getArgument(0, RequestPaymentHistory.class);
//        System.out.println("Handling payment history request user - " + paymentHistoryRequest.getUserId());
//        List<ReplyPaymentHistory> historyList = paymentHistory.getHistory(paymentHistoryRequest.getUserId(), paymentHistoryRequest.getUserType());
//        var replyEvent = new ReplyPaymentHistoryExtended(
//                paymentHistoryRequest.getCorrelationId(),
//                historyList
//        );
//        queue.publish(new Event(
//                ReplyPaymentHistoryExtended.topic,
//                new Object[]{replyEvent}
//        ));
    }

    private void publishErrorDuringPayment(UUID correlationId, String message) {
        queue.publish(new Event(
                PayReplied.topic,
                new Object[]{new PayReplied(
                        correlationId,
                        null,
                        new PayRepliedFailure(message)
                )}
        ));
    }

}
