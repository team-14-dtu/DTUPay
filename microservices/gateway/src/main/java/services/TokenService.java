package services;
import event.token.TokensReplied;
import event.token.TokensRequested;
import messaging.implementations.RabbitMqQueue;
import messaging.Event;
import messaging.MessageQueue;
import sharedMisc.QueueUtils;
import team14messaging.ReplyWaiter;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class TokenService {

    private final MessageQueue queue = new RabbitMqQueue(QueueUtils.getQueueName());
    private final ReplyWaiter waiter = new ReplyWaiter(queue,
            TokensReplied.topic
    );

    private CompletableFuture<TokensReplied> replyToken;

    public TokenService() {
        //queue.addHandler(TokensReplied.topic, this::tokenReceived);
    }

    /*public void tokenReceived(Event event) {
        var response = event.getArgument(0, List.class);
        List<Token> tokens = (List<Token>) response;
        // TODO
        replyToken.complete(new TokensReplied("TODO", tokens));
    }*/

    public TokensReplied requestTokens(UUID cid, int numberOfTokens) {
        final UUID correlationId = UUID.randomUUID();

        waiter.registerWaiterForCorrelation(correlationId);

        queue.publish(new Event(TokensRequested.topic, new Object[] {
                new TokensRequested(correlationId,cid,numberOfTokens)
        }));

        var event = waiter.synchronouslyWaitForReply(
                correlationId
        );

        var reply = event.getArgument(0, TokensReplied.class);


        return reply;
    }
}