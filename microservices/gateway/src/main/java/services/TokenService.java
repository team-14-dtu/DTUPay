package services;

import event.token.TokensReplied;
import event.token.TokensRequested;
import messaging.Event;
import messaging.MessageQueue;
import team14messaging.ReplyWaiter;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

// @author : Mathilde
@ApplicationScoped
public class TokenService {
    private final MessageQueue queue;
    private final ReplyWaiter waiter;

    public TokenService(MessageQueue mq) {
        queue = mq;
        waiter = new ReplyWaiter(queue,
                TokensReplied.topic
        );
    }

    public TokensReplied requestTokens(UUID cid, int numberOfTokens) {
        final UUID correlationId = UUID.randomUUID();

        waiter.registerWaiterForCorrelation(correlationId);

        queue.publish(new Event(TokensRequested.topic, new Object[]{
                new TokensRequested(correlationId, cid, numberOfTokens)
        }));

        Event event = waiter.synchronouslyWaitForReply(correlationId);

        TokensReplied reply = event.getArgument(0, TokensReplied.class);

        return reply;
    }
}