package services;

import event.CreateUser;
import event.token.TokensRequested;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.User;
import sharedMisc.QueueUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class Service {

    private final MessageQueue queue = new RabbitMqQueue(QueueUtils.getQueueName());

//    public services.Service(MessageQueue queue) {
//        this.queue = queue;
//    }

    // TODO: look into threading
    private CompletableFuture<CreateUser> userCreated;

    private CompletableFuture<TokensRequested> requestToken;

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
        return new User(result.getName(), UUID.nameUUIDFromBytes(("1").getBytes()));
    }
}