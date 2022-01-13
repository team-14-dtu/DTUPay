import db.PaymentHistory;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.Payment;
import rest.User;
import sharedMisc.QueueUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static event.payment.PaymentEvents.*;

public class PaymentService {

    Map<String, String> tokenDatabase = Map.of("token1", "cid");

    PaymentService() {
        System.out.println("Payment service running...");
        queue.addHandler(getPaymentRequestTopics(), this::unpackPaymentEvent);
        queue.addHandler(getHistoryRequestTopics(), this::unpackHistoryEvent);
        queue.addHandler(getAllHistoryRequestTopics(), this::unpackAllHistoryEvent);
        queue.addHandler(getTargetPaymentRequestTopics(), this::unpackTargetPaymentEvent);
    }

    private final MessageQueue queue = new RabbitMqQueue(QueueUtils.getQueueName());
    PaymentHistory paymentHistory = new PaymentHistory();

    public static void main(String[] args) throws InterruptedException {
        PaymentService paymentService = new PaymentService();
    }

    private void unpackPaymentEvent(Event event) {
        Payment payment = event.getArgument(0, Payment.class);
        payment.setDebtorId(tokenDatabase.get(payment.getId())); //TODO replace with sending an event to the Token Manager and waiting for the response
        paymentHistory.setPaymentHistory(payment);
    }

    //TODO create consumer to unpack an event and send the id to the payment history
    private void unpackHistoryEvent(Event event) {
        String id = event.getArgument(0, String.class);
        List<Payment> payments = paymentHistory.getPaymentsForUser(id, User.Type.CUSTOMER); //TODO receive user type
        queue.publish(new Event(getHistoryRequestGatewayTopics(), new Object[]{payments}));
    }

    private void unpackAllHistoryEvent(Event event) {
        List<Payment> payments = paymentHistory.getAllPayments();
        queue.publish(new Event(getAllHistoryRequestGatewayTopics(), new Object[]{payments}));
    }

    private void unpackTargetPaymentEvent(Event event) {
        String paymentId = event.getArgument(0, String.class);
        Payment payment = paymentHistory.getTargetPayment(paymentId); //TODO receive user type
        queue.publish(new Event(getTargetPaymentRequestGatewayTopics(), new Object[]{payment}));
    }
}
