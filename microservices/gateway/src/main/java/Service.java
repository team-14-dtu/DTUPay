import event.CreateUser;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class Service {

    private final MessageQueue queue = new RabbitMqQueue(Constants.queueName);

//    public Service(MessageQueue queue) {
//        this.queue = queue;
//    }

    // TODO: look into threading
    private CompletableFuture<CreateUser> userCreated;

    public Service() {
        queue.addHandler(Constants.eventTypeCreateUserRequest, this::userCreatedConsumer);
    }

    private void userCreatedConsumer(Event event) {
        var s = event.getArgument(0, CreateUser.class);
        userCreated.complete(s);
    }

    public User hello() {
        userCreated = new CompletableFuture<>();
        queue.publish(new Event(
                Constants.eventTypeCreateUserRequest,
                new Object[]{new CreateUser("Petr event")
                }));
        final var result = userCreated.join();
        return new User(result.getName(), "1");
    }
}