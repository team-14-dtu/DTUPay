package services;

import event.CreateUser;
import event.token.RequestTokens;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.Token;
import rest.User;
import sharedMisc.QueueUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class TokenService {

    private final MessageQueue queue = new RabbitMqQueue(QueueUtils.getQueueName());

//    public services.Service(MessageQueue queue) {
//        this.queue = queue;
//    }

    // TODO: look into threading
    private CompletableFuture<RequestTokens> requestToken;

    public TokenService() {
        queue.addHandler(RequestTokens.getEventName(), this::tokenRecieved);
    }

    private void tokenRecieved(Event event) {
        var s = event.getArgument(0, RequestTokens.class);
        requestToken.complete(s);
    }

    public List<Token> requestTokens(String customerId, int numberOfTokens) {
        requestToken = new CompletableFuture<>();
        queue.publish(new Event(
                RequestTokens.getEventName(),
                new Object[] {new RequestTokens(customerId,numberOfTokens)
                }));
        final var result = requestToken.join();
        return null;
    }
}