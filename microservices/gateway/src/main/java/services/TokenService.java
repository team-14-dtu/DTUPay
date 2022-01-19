package services;
import event.token.TokensReplied;
import event.token.TokensRequested;
import messaging.implementations.RabbitMqQueue;
import messaging.Event;
import messaging.MessageQueue;
import services.errors.DTUPayError;
import sharedMisc.QueueUtils;
import team14messaging.ReplyWaiter;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class TokenService {

    private final MessageQueue queue;
    private final ReplyWaiter waiter;

    private CompletableFuture<TokensReplied> replyToken;

    public TokenService(MessageQueue mq) {
        queue = mq;
        waiter = new ReplyWaiter(queue,
                TokensReplied.topic
        );
    }

    public TokensReplied requestTokens(UUID cid, int numberOfTokens) {
        final UUID correlationId = UUID.randomUUID();

        waiter.registerWaiterForCorrelation(correlationId);

        queue.publish(new Event(TokensRequested.topic, new Object[] {
                new TokensRequested(correlationId,cid,numberOfTokens)
        }));

        var event = waiter.synchronouslyWaitForReply(
                correlationId
        );

        TokensReplied reply = event.getArgument(0, TokensReplied.class);

        return reply;
        /*if (reply.isSuccess()) {
            return new TokensReplied(correlationId,new TokensReplied.Success(reply.getSuccessResponse().getTokens()));
        } else {
            return new TokensReplied(correlationId,new TokensReplied.Failure(reply.getFailureResponse().getTokens()));
        }*/
    }
}