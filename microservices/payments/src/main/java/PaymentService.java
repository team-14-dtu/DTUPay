import db.PaymentHistory;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.Payment;
import rest.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static event.payment.PaymentEvents.*;

public class PaymentService {

    Map<String, String> tokenDatabase = Map.of("tokenId1", "customerId");

    PaymentService() {
        System.out.println("Payment service running...");
        queue.addHandler(getPaymentRequestTopics(), this::unpackPaymentEvent);
        queue.addHandler(getHistoryRequestTopics(), this::unpackHistoryEvent);
        queue.addHandler(getTargetPaymentRequestTopics(), this::unpackTargetPaymentEvent);
    }

    private final MessageQueue queue = new RabbitMqQueue("localhost"); //TODO: Change this when I dockerize it...
    PaymentHistory paymentHistory = new PaymentHistory();
    private final BankService bank = new BankServiceService().getBankServicePort();

    public static void main(String[] args) throws InterruptedException {
        PaymentService paymentService = new PaymentService();
    }

    private void unpackPaymentEvent(Event event) {
        Payment payment = event.getArgument(0, Payment.class);
        //payment.setDebtorId(tokenDatabase.get(payment.getId())); //TODO replace with sending an event to the Token Manager and waiting for the response

        try {
            bank.transferMoneyFromTo(payment.getDebtorId(), payment.getCreditorId(), payment.getAmount(), payment.getDescription());
        } catch (BankServiceException_Exception e) {
            System.out.println("An error occured in bankservice money transfer.");
        }

        paymentHistory.addPaymentHistory(payment);
        queue.publish(new Event(getPaymentRequestGatewayTopics(), new Object[]{payment}));
    }

    private void unpackHistoryEvent(Event event) {
        String id = event.getArgument(0, String.class);
        User.Type type = event.getArgument(1, User.Type.class);
        List<Payment> payments = paymentHistory.getPaymentsForUser(id, type);
        queue.publish(new Event(getHistoryRequestGatewayTopics(), new Object[]{payments}));
    }

    private void unpackTargetPaymentEvent(Event event) {
        UUID paymentId = event.getArgument(0, UUID.class);
        Payment payment = paymentHistory.getTargetPayment(paymentId); //TODO receive user type
        queue.publish(new Event(getTargetPaymentRequestGatewayTopics(), new Object[]{payment}));
    }
}
