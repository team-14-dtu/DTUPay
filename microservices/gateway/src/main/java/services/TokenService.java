package services;

import event.payment.history.PaymentHistoryExtendedReplied;
import event.payment.history.PaymentHistoryReplied;
import event.payment.pay.PayReplied;
import event.token.TokensReplied;
import event.token.TokensRequested;
import messaging.implementations.RabbitMqQueue;
import messaging.Event;
import messaging.MessageQueue;
import rest.Token;
//import rest.TokensRequested;
import sharedMisc.QueueUtils;
import team14messaging.ReplyWaiter;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
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
        final String correlationId = UUID.randomUUID().toString();

        waiter.registerWaiterForCorrelation(correlationId);

        System.out.println("Received REST message for: requesting tokens");

        queue.publish(new Event(
                TokensRequested.topic,
                new Object[] {cid, numberOfTokens}));

        var event = waiter.synchronouslyWaitForReply(
                correlationId
        );

        var reply = event.getArgument(0, TokensReplied.class);


        return reply;
    }
}