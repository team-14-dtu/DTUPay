package services;

import event.account.BankAccountIdFromCustomerIdRequested;
import event.token.CustomerIdFromTokenRequested;
import event.token.TokensReplied;
import event.token.TokensRequested;
import messaging.Event;
import messaging.MessageQueue;
import services.db.Database;
import services.exceptions.CanNotGenerateTokensException;
import services.exceptions.CustomerNotFoundException;

import java.util.List;
import java.util.UUID;

public class TokenManagementService {

    public final Database database;
    private final MessageQueue queue;

    // @author : Mathilde
    public TokenManagementService(MessageQueue mq, Database db) {
        queue = mq;
        database = db;

        System.out.println("token management service running");

        queue.addHandler(TokensRequested.topic, this::handleRequestTokens);
        queue.addHandler(CustomerIdFromTokenRequested.topic, this::handleRequestCustomerIdFromToken);
    }
    // @author : Naja
    public void handleRequestCustomerIdFromToken(Event event) {
        final CustomerIdFromTokenRequested request =
                event.getArgument(0, CustomerIdFromTokenRequested.class);

        BankAccountIdFromCustomerIdRequested br;
        try {
            br = new BankAccountIdFromCustomerIdRequested(
                    request.getCorrelationId(),
                    new BankAccountIdFromCustomerIdRequested.BRSuccess(database.findCustomerFromTokenId(request.getTokenId())));

        } catch (CustomerNotFoundException e) {
            br = new BankAccountIdFromCustomerIdRequested(
                    request.getCorrelationId(),
                    new BankAccountIdFromCustomerIdRequested.BRFailure(e.getMessage()));
        }
        queue.publish(
                new Event(
                        BankAccountIdFromCustomerIdRequested.topic,
                        new Object[]{br}
                )
        );
    }

    // @author : Mathilde
    public void handleRequestTokens(Event event) {
        final var request = event.getArgument(0, TokensRequested.class);

        TokensReplied replyEvent;
        try {
            List<UUID> tokens = database.generateNewTokens(request.getCid(), request.getNoOfTokens());
            replyEvent = new TokensReplied(
                    request.getCorrelationId(),
                    new TokensReplied.Success(tokens)
            );
        } catch (CanNotGenerateTokensException e) {
            System.out.println(e.getMessage());
            replyEvent = new TokensReplied(
                    request.getCorrelationId(),
                    new TokensReplied.Failure(
                            database.getTokens(request.getCid()),
                            e.getMessage()
                    )
            );
        }

        queue.publish(
                new Event(
                        TokensReplied.topic,
                        new Object[]{
                                replyEvent
                        }
                )
        );
    }
}
