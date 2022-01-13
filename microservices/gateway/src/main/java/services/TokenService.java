package services;

import event.token.ReplyTokens;
import event.token.RequestTokens;
import messaging.Event;
import messaging.MessageQueue;
import rest.Token;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class TokenService {

    private final MessageQueue queue;
    private CompletableFuture<List<Token>> replyToken;

    public TokenService(MessageQueue q) {
        queue = q;
        queue.addHandler(ReplyTokens.getEventName(), this::tokenReceived);
    }

    public void tokenReceived(Event event) {
        var response = event.getArgument(0, List.class);
        List<Token> tokens = (List<Token>) response;
        replyToken.complete(tokens);
    }

    public List<Token> requestTokens(String cid, int numberOfTokens) {
        System.out.println("Received REST message for: requesting tokens");
        replyToken = new CompletableFuture<>();
        System.out.println("After reply token");
        queue.publish(new Event(
                RequestTokens.getEventName(),
                new Object[] {cid, numberOfTokens}));
        final var result = replyToken.join();
        return result;
    }
}