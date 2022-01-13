import db.PaymentHistory;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.Payment;
import rest.User;
import sharedMisc.QueueUtils;

import java.util.Arrays;
import java.util.List;

import static event.payment.PaymentEvents.*;

public class PaymentService {

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
        List<String> payment = Arrays.asList(event.getArgument(0, String.class).split("\\s*,\\s*"));
        String paymentId = payment.get(0);
        String customerId = payment.get(1);
        String merchantId = payment.get(2);
        String amount = payment.get(3);
        String description = payment.get(4);
        paymentHistory.setPaymentHistory(paymentId, customerId, merchantId, amount, description);
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
