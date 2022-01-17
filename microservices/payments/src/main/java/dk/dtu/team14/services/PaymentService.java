package dk.dtu.team14.services;

import dk.dtu.team14.db.PaymentHistory;
import event.payment.history.ReplyPaymentHistory;
import event.payment.history.ReplyPaymentHistoryExtended;
import event.payment.history.RequestPaymentHistory;
import event.payment.pay.ReplyPay;
import event.payment.pay.ReplyPayFailure;
import event.payment.pay.ReplyPaySuccess;
import event.payment.pay.RequestPay;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.Payment;
import java.util.List;

public class PaymentService {

    public PaymentService() {}

    private final MessageQueue queue = new RabbitMqQueue("localhost"); //TODO: Change this when I dockerize it...
    PaymentHistory paymentHistory = new PaymentHistory();
    private final BankService bank = new BankServiceService().getBankServicePort();

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
        final var payRequest = event.getArgument(0, RequestPay.class);
        System.out.println("Handling pay request - " + payRequest.getId());
        try {
            bank.transferMoneyFromTo(payRequest.getCustomerId(), payRequest.getMerchantId(), payRequest.getAmount(), payRequest.getDescription());
        } catch (BankServiceException_Exception e) {
            publishErrorDuringPayment(
                    payRequest.getId(),
                    payRequest.getCorrelationId(),
                    "Bankservice payment error");
            return;
        }
        paymentHistory.addPaymentHistory(new Payment(payRequest.getId(), payRequest.getMerchantId(), payRequest.getCustomerId(), payRequest.getAmount(), payRequest.getDescription()));
        var replyEvent = new ReplyPay(
                payRequest.getCorrelationId(),
                payRequest.getId(),
                new ReplyPaySuccess(
                        payRequest.getId(),
                        payRequest.getAmount(),
                        payRequest.getDescription()
                ),
                null
        );

        queue.publish(new Event(
                ReplyPay.topic,
                new Object[]{replyEvent}
        ));
    }

    public void handlePaymentHistoryRequest(Event event) {
        final var paymentHistoryRequest = event.getArgument(0, RequestPaymentHistory.class);
        System.out.println("Handling payment history request user - " + paymentHistoryRequest.getUserId());
        List<ReplyPaymentHistory> historyList = paymentHistory.getHistory(paymentHistoryRequest.getUserId(), paymentHistoryRequest.getUserType());
        var replyEvent = new ReplyPaymentHistoryExtended(
                paymentHistoryRequest.getCorrelationId(),
                historyList
        );
        queue.publish(new Event(
                ReplyPaymentHistoryExtended.topic,
                new Object[]{replyEvent}
        ));
    }

    private void publishErrorDuringPayment(String id, String correlationId, String message) {
        queue.publish(new Event(
                ReplyPay.topic,
                new Object[]{new ReplyPay(
                        correlationId,
                        id,
                        null,
                        new ReplyPayFailure(message)
                )}
        ));
    }

}
