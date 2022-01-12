import event.payments.CreatePayment;
import event.CreateUser;
import event.payments.GetPayments;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.Payment;
import rest.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@ApplicationScoped
public class Service {

    private final MessageQueue queue = new RabbitMqQueue(QueueNames.getQueueName());


//    public Service(MessageQueue queue) {
//        this.queue = queue;
//    }

    // TODO: look into threading
    private CompletableFuture<CreateUser> userCreated;
    private CompletableFuture<CreatePayment> paymentCreated;
    public CompletableFuture<GetPayments> paymentGot;

    public List<Payment> payments;

    public Service() {
        queue.addHandler(CreateUser.getEventName(), this::userCreatedConsumer);

        queue.addHandler(CreatePayment.getEventName(), this::paymentCreatedConsumer);
        queue.addHandler(GetPayments.getEventName(), this::paymentGetConsumer);

    }

    private void userCreatedConsumer(Event event) {
        var s = event.getArgument(0, CreateUser.class);
        userCreated.complete(s);
    }

    private void paymentCreatedConsumer(Event event) {
        var payment = event.getArgument(0, CreatePayment.class);
        paymentCreated.complete(payment);
    }

    private void paymentGetConsumer(Event event) {
        var payments = event.getArgument(0, GetPayments.class);
        //this.payments = event.;
        paymentGot.complete(payments);
    }

    public User hello() {
        System.out.println(System.getProperty("vertxweb.environment"));
        userCreated = new CompletableFuture<>();
        queue.publish(new Event(
                CreateUser.getEventName(),
                new Object[]{new CreateUser("Petr")
                }));
        final var result = userCreated.join();
        return new User(result.getName(), "1");
    }

    public void publishEvent(Event event) {
        queue.publish(event);
    }

    public void consumeEvent(String eventType, Consumer<Event> consumer) {
        queue.addHandler(eventType, consumer);
    }
}