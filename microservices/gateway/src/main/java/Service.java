import event.CreateUser;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class Service {

    private final MessageQueue queue = new RabbitMqQueue(QueueNames.getQueueName());

//    public Service(MessageQueue queue) {
//        this.queue = queue;
//    }

    // TODO: look into threading
    private CompletableFuture<CreateUser> userCreated;

    public Service() {
        queue.addHandler(QueueNames.eventTypeCreateUserRequest, this::userCreatedConsumer);
    }

    private void userCreatedConsumer(Event event) {
        var s = event.getArgument(0, CreateUser.class);
        userCreated.complete(s);
    }

    public User hello() {
        System.out.println(System.getProperty("vertxweb.environment"));
        userCreated = new CompletableFuture<>();
        queue.publish(new Event(
                QueueNames.eventTypeCreateUserRequest,
                new Object[]{new CreateUser("Petr")
                }));
        final var result = userCreated.join();
        return new User(result.getName(), "1");
    }
}