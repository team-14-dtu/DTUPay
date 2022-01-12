import db.PaymentHistory;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.Payment;
import rest.User;

import java.util.List;

public class PaymentService {

    PaymentService() {
        System.out.println("payment service running");
        queue.addHandler("PAYMENT.HISTORY_REQUEST", this::unpackHistoryEvent);
    }

    private final MessageQueue queue = new RabbitMqQueue(event.QueueNames.getQueueName());
    PaymentHistory paymentHistory = new PaymentHistory();

    public static void main(String[] args) throws InterruptedException {
        PaymentService paymentService = new PaymentService();
    }


    //TODO create consumer to unpack an event and send the id to the payment history
    private void unpackHistoryEvent(Event event)
    {
        System.out.println("test to see if message got consumed");
        String id = event.getArgument(0, String.class);
        List<Payment> payments = paymentHistory.getPaymentsForUser(id, User.Type.CUSTOMER); //TODO receive user type
        queue.publish(new Event("GATEWAY.HISTORY_REQUEST", new Object[]{payments}));
    }
}
