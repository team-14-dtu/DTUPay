package dk.dtu.team14.services;

import dk.dtu.team14.db.PaymentHistory;
import event.account.ReplyBankAccountIdFromCustomerId;
import event.account.ReplyBankAccountIdFromMerchantId;
import event.account.RequestBankAccountIdFromMerchantId;
import event.payment.history.RequestPaymentHistory;
import event.payment.pay.ReplyPay;
import event.payment.pay.ReplyPaySuccess;
import event.payment.pay.RequestPay;
import event.token.RequestCustomerIdFromToken;
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

    public PaymentService() {
    }

    private final MessageQueue queue = new RabbitMqQueue(QueueUtils.getQueueName("dev")); //TODO: the queue name needs to be fixed...
    private final ReplyWaiter waiter = new ReplyWaiter(
            queue,
            ReplyBankAccountIdFromCustomerId.topic,
            ReplyBankAccountIdFromMerchantId.topic
    );
    private final PaymentHistory paymentHistory = new PaymentHistory();
    private final BankService bank = new BankServiceService().getBankServicePort();

    private Executor executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        System.out.println("Payment service running...");
        PaymentService paymentService = new PaymentService();
        paymentService.handleIncomingMessages();
    }

    public void handleIncomingMessages() {
        queue.addHandler(RequestPaymentHistory.topic, this::handlePaymentHistoryRequest);
        queue.addHandler(RequestPay.topic, this::handlePayRequest);
    }

    public void handlePayRequest(Event event) {
        executor.execute(() -> {
            final var payRequest = event.getArgument(0, RequestPay.class);
            System.out.println("Handling pay request - " + payRequest.getCorrelationId());

            // Get the customer id. We will sent a request to token service, which
            // will sent a request to account service and reply with both customer id
            // and bankAccountId. We also need to request merchant bank account id
            // from the account service

            final String merchantBankAccountRequestCorrelationId = UUID.randomUUID().toString();
            final String customerIdAndBankAccountFromTokenId = UUID.randomUUID().toString();

            waiter.registerWaiterForCorrelation(merchantBankAccountRequestCorrelationId);
            waiter.registerWaiterForCorrelation(customerIdAndBankAccountFromTokenId);

            queue.publish(new Event(
                    RequestBankAccountIdFromMerchantId.topic,
                    new Object[]{
                            new RequestBankAccountIdFromMerchantId(
                                    merchantBankAccountRequestCorrelationId,
                                    payRequest.getMerchantId()
                            )
                    }
            ));

            queue.publish(
                    new Event(
                            RequestCustomerIdFromToken.topic,
                            new Object[]{
                                    new RequestCustomerIdFromToken(
                                            customerIdAndBankAccountFromTokenId,
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
                    waiter.synchronouslyWaitForReply(customerIdAndBankAccountFromTokenId);

            System.out.println("here1");
            final ReplyBankAccountIdFromMerchantId merchantBankAccount =
                    merchantBankAccountIdResponse.getArgument(0, ReplyBankAccountIdFromMerchantId.class);
            System.out.println("here2");
            final ReplyBankAccountIdFromCustomerId customerBankAccountAndId =
                    customerIdAndBankAccountFromTokenIdResponse.getArgument(0, ReplyBankAccountIdFromCustomerId.class);
            System.out.println("here3");
//        try {
//            bank.transferMoneyFromTo(
//                    customerBankAccountAndId.getBankAccountId(),
//                    merchantBankAccount.getBankAccountId(),
//                    payRequest.getAmount(),
//                    payRequest.getDescription()
//            );
//        } catch (BankServiceException_Exception e) {
//            publishErrorDuringPayment(
//                    payRequest.getCorrelationId(),
//                    "Bankservice payment error");
//            return;
//        }

//        paymentHistory.addPaymentHistory(new Payment(payRequest.getId(), payRequest.getMerchantId(), payRequest.getTokenId(), payRequest.getAmount(), payRequest.getDescription()));

            var replyEvent = new ReplyPay(
                    payRequest.getCorrelationId(),
                    new ReplyPaySuccess(
                            "0f5de96a-c50b-4010-bbfa-5f5d8e1af693",
                            payRequest.getAmount(),
                            payRequest.getDescription()
                    ),
                    null
            );

            queue.publish(new Event(
                    ReplyPay.topic,
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

    private void publishErrorDuringPayment(String correlationId, String message) {
//        queue.publish(new Event(
//                ReplyPay.topic,
//                new Object[]{new ReplyPay(
//                        correlationId,
//                        id,
//                        null,
//                        new ReplyPayFailure(message)
//                )}
//        ));
    }

}
