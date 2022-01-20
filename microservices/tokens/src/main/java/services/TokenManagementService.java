package services;

import event.account.BankAccountIdFromCustomerIdRequested;
import event.token.TokensReplied;
import event.token.CustomerIdFromTokenRequested;
import event.token.TokensRequested;
import messaging.Event;
import messaging.MessageQueue;

import java.util.*;

import services.db.Database;
import services.exceptions.CanNotGenerateTokensException;
import services.exceptions.CustomerNotFoundException;

public class TokenManagementService {

    public final Database database;
    private final MessageQueue queue;

    public TokenManagementService(MessageQueue mq, Database db) {
        queue = mq;
        database = db;

        System.out.println("token management service running");

        queue.addHandler(TokensRequested.topic, this::handleRequestTokens);
        queue.addHandler(CustomerIdFromTokenRequested.topic, this::handleRequestCustomerIdFromToken);
    }

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
                        new Object[] {br}
                )
        );
    }

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
