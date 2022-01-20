package services.services;

import services.db.PaymentHistory;
import services.data.Payment;
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PaymentService {
    private final MessageQueue queue;
    private final ReplyWaiter waiter;
    private BankService bank;
    private final PaymentHistory paymentHistory = new PaymentHistory();

    public PaymentService(MessageQueue mq, BankService bank, ReplyWaiter waiter) {
        queue = mq;
        this.waiter = waiter;
        this.bank = bank;
    }

    public PaymentHistory getPaymentHistory() {
        return paymentHistory;
    }

    public static void main(String[] args) {
        System.out.println("Payment service running...");
        var messageQueue = new RabbitMqQueue(QueueUtils.getQueueName(args[0]));
        var bank = new BankServiceService().getBankServicePort();
        var waiter = new ReplyWaiter(
                messageQueue,
                BankAccountIdFromCustomerIdReplied.topic,
                BankAccountIdFromMerchantIdReplied.topic
        );
        PaymentService paymentService = new PaymentService(messageQueue, bank, waiter);
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
            //if (bank.doesBankAccountExist()) TODO does bank account exist function does not exist, bank will instead throw an exceptions already caught below
            if (!merchantBankAccount.isSuccess())
            {
                publishErrorDuringPayment(payRequest.getCorrelationId(), merchantBankAccount.getFailureResponse().getReason());
                return;
            }
            if (!customerBankAccountAndId.isSuccess())
            {
                publishErrorDuringPayment(payRequest.getCorrelationId(), customerBankAccountAndId.getFailureResponse().getReason());
                return;
            }
            if (payRequest.getAmount().compareTo(BigDecimal.ZERO) != 1)
            {
                publishErrorDuringPayment(payRequest.getCorrelationId(), "Payment amount must be positive");
                return;
            }
            if (bank.getAccount(customerBankAccountAndId.getSuccessResponse().getBankAccountId()).getBalance().compareTo(payRequest.getAmount()) == -1)
            {
                publishErrorDuringPayment(payRequest.getCorrelationId(), "Insufficient balance");
                return;
            }
            bank.transferMoneyFromTo(
                    customerBankAccountAndId.getSuccessResponse().getBankAccountId(),
                    merchantBankAccount.getSuccessResponse().getBankAccountId(),
                    payRequest.getAmount(),
                    payRequest.getDescription()
            );
        } catch (BankServiceException_Exception e) {
            publishErrorDuringPayment(
                    payRequest.getCorrelationId(),
                    "BankService failure");
            return;
        }

        UUID paymentId = UUID.randomUUID();
        paymentHistory.addPaymentHistory(paymentId, new Payment(customerBankAccountAndId.getSuccessResponse().getCustomerId(), payRequest.getMerchantId(), payRequest.getAmount(), payRequest.getDescription(), new Timestamp(System.currentTimeMillis()), customerBankAccountAndId.getSuccessResponse().getCustomerName(), merchantBankAccount.getSuccessResponse().getMerchantName()));

        var replyEvent = new PayReplied(
                payRequest.getCorrelationId(),
                new PayReplied.PayRepliedSuccess(
                        paymentId,
                        payRequest.getAmount(),
                        payRequest.getDescription()
                )
        );

        queue.publish(new Event(
                PayReplied.topic,
                new Object[]{replyEvent}
        ));

    }

    public void handlePaymentCustomerHistoryRequest(Event event) {
        final var paymentCustomerHistoryRequest = event.getArgument(0, PaymentHistoryRequested.PaymentCustomerHistoryRequested.class);
        System.out.println("Handling payment history request user - " + paymentCustomerHistoryRequest.getCustomerId());

//        if (checkCustomerAccount(paymentCustomerHistoryRequest.getCustomerId()).getFailureResponse() != null) {
//            //TODO: return failure message
//        }

        List<PaymentHistoryCustomer> customerHistoryList = paymentHistory.getCustomerHistory(paymentCustomerHistoryRequest.getCustomerId());

//        if (customerHistoryList.size() == 0) {
//            //TODO: return empty list failure message
//        }

        var replyEvent = new PaymentHistoryReplied.PaymentCustomerHistoryReplied(
                paymentCustomerHistoryRequest.getCorrelationId(),
                new PaymentHistoryReplied.PaymentCustomerHistoryReplied.PaymentCustomerHistoryRepliedSuccess(customerHistoryList)
        );
        queue.publish(new Event(
                PaymentHistoryReplied.PaymentCustomerHistoryReplied.topic,
                new Object[]{replyEvent}
        ));
    }

    public void handlePaymentMerchantHistoryRequest(Event event) {
        final var paymentMerchantHistoryRequest = event.getArgument(0, PaymentHistoryRequested.PaymentMerchantHistoryRequested.class);
        System.out.println("Handling payment history request user - " + paymentMerchantHistoryRequest.getMerchantId());

//        if (checkCustomerAccount(paymentCustomerHistoryRequest.getCustomerId()).getFailureResponse() != null) {
//            //TODO: return failure message
//        }

        List<PaymentHistoryMerchant> merchantHistoryList = paymentHistory.getMerchantHistory(paymentMerchantHistoryRequest.getMerchantId());

//        if (merchantHistoryList.size() == 0) {
//            //TODO: return empty list failure message
//        }

        var replyEvent = new PaymentHistoryReplied.PaymentMerchantHistoryReplied(
                paymentMerchantHistoryRequest.getCorrelationId(),
                new PaymentHistoryReplied.PaymentMerchantHistoryReplied.PaymentMerchantHistoryRepliedSuccess(merchantHistoryList)
        );
        queue.publish(new Event(
                PaymentHistoryReplied.PaymentMerchantHistoryReplied.topic,
                new Object[]{replyEvent}
        ));
    }

    public void handlePaymentManagerHistoryRequest(Event event) {
        final var paymentManagerHistoryRequest = event.getArgument(0, PaymentHistoryRequested.PaymentManagerHistoryRequested.class);
        System.out.println("Handling payment history request user - manager");
        List<PaymentHistoryManager> managerHistoryList = paymentHistory.getManagerHistory();

//        if (managerHistoryList.size() == 0) {
//            //TODO: return empty list failure message
//        }

        var replyEvent = new PaymentHistoryReplied.PaymentManagerHistoryReplied(
                paymentManagerHistoryRequest.getCorrelationId(),
                new PaymentHistoryReplied.PaymentManagerHistoryReplied.PaymentManagerHistoryRepliedSuccess(managerHistoryList)
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

//    private BankAccountIdFromCustomerIdReplied checkCustomerAccount(UUID customerId) {
//        //TODO
//        return null;
//    }
//
//    private BankAccountIdFromMerchantIdReplied checkMerchantAccount(UUID merchantId) {
//        //TODO
//        return null;
//    }

}
