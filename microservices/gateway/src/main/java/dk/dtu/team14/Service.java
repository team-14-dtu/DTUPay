package dk.dtu.team14;

import event.QueueNames;
import event.CreateUser;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.Payment;
import rest.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class Service {

    private final MessageQueue queue = new RabbitMqQueue(QueueNames.getQueueName());

    // TODO: look into threading
    private CompletableFuture<CreateUser> userCreated;

    public Service() {
        queue.addHandler(CreateUser.getEventName(), this::userCreatedConsumer);
    }

    private void userCreatedConsumer(Event event) {
        var s = event.getArgument(0, CreateUser.class);
        userCreated.complete(s);
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
}