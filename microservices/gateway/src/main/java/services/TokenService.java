package services;

import event.token.TokensReplied;
import event.token.TokensRequested;
import messaging.Event;
import messaging.MessageQueue;
import rest.Token;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class TokenService {

    private final MessageQueue queue;
    private CompletableFuture<TokensReplied> replyToken;

    public TokenService(MessageQueue q) {
        queue = q;
        queue.addHandler(TokensReplied.getEventName(), this::tokenReceived);
    }

    public void tokenReceived(Event event) {
        var response = event.getArgument(0, List.class);
        List<Token> tokens = (List<Token>) response;
        replyToken.complete(new TokensReplied(tokens));
    }

    public TokensReplied requestTokens(UUID cid, int numberOfTokens) {
        System.out.println("Received REST message for: requesting tokens");
        replyToken = new CompletableFuture<>();
        System.out.println("After reply token");
        queue.publish(new Event(
                TokensRequested.getEventName(),
                new Object[] {cid, numberOfTokens}));
        final var result = replyToken.join();
        return result;
    }
}