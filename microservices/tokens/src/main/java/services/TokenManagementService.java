package services;

import event.account.BankAccountIdFromCustomerIdRequested;
import event.token.TokensReplied;
import event.token.CustomerIdFromTokenRequested;
import event.token.TokensRequested;
import messaging.Event;
import messaging.MessageQueue;

import java.util.*;
import java.util.stream.Collectors;

public class TokenManagementService {

    public HashMap<UUID, List<UUID>> tokenDatabase = new HashMap<>() {};

    private final MessageQueue queue;

    public TokenManagementService(MessageQueue mq) {
        queue = mq;
        System.out.println("token management service running");
        //queue.addHandler(TokensRequested.topic, this::generateTokensEvent);
        queue.addHandler(TokensRequested.topic, this::handleRequestTokens);
        queue.addHandler(CustomerIdFromTokenRequested.topic, this::handleRequestCustomerIdFromToken);
    }

    private void handleRequestCustomerIdFromToken(Event event) {
        final CustomerIdFromTokenRequested request =
                event.getArgument(0, CustomerIdFromTokenRequested.class);

        System.out.println("Handling event in token management: " + request.getCorrelationId());

        queue.publish(
                new Event(
                        BankAccountIdFromCustomerIdRequested.topic,
                        new Object[]{
                                new BankAccountIdFromCustomerIdRequested(
                                        request.getCorrelationId(),
                                        findCustomerFromTokenId(request.getTokenId())//TODO: replace with findCustomerFromTokenId(request.getTokenId()) when gen-tokens are established
                                )
                        }
                )
        );
    }

    private String findCustomerFromTokenId(UUID tokenId) {

        for (UUID cid : tokenDatabase.keySet()) {
            if (tokenDatabase.get(cid).contains(tokenId)) {
                invalidateToken(tokenId, cid);
                System.out.println("Customer: " + cid.toString());
                return cid.toString();
            }
        }
        return "Token not found"; //TODO: Actually handle the errors
    }

    private void invalidateToken(UUID tokenId, UUID cid) {
        List<UUID> tokenIds = tokenDatabase.get(cid);
        tokenIds.remove(tokenId);

        tokenDatabase.remove(cid);
        tokenDatabase.put(cid,tokenIds);
    }

    private void handleRequestTokens(Event event) {
        final var request = event.getArgument(0, TokensRequested.class);

        System.out.println("Handling event in token management: " + request.getCorrelationId());

        List<UUID> tokens = generateNewTokens(request.getCid(), request.getNoOfTokens());
        queue.publish(
                new Event(
                        TokensReplied.topic,
                        new Object[]{
                                new TokensReplied(
                                        request.getCorrelationId(),
                                        tokens
                                )
                        }
                )
        );
    }
    public List<UUID> generateNewTokens(UUID cid, int numberOfTokens) {

        if (!tokenDatabase.containsKey(cid)) {
            tokenDatabase.put(cid, new ArrayList<>());
        }

        List<UUID> currentTokensOfCustomer = tokenDatabase.get(cid);

        if (currentTokensOfCustomer.size() <= 1) {

            //Create tokens
            System.out.println("Generating "+numberOfTokens+" new tokens");
            for (int i=0; i<numberOfTokens; i++ ) {
                UUID newToken = UUID.randomUUID();
                tokenDatabase.get(cid).add(newToken);
            }
        }
        return tokenDatabase.get(cid);
    }

    /*public void generateTokensEvent(Event event) {
        System.out.println("test to see if message got consumed");
        UUID cid = event.getArgument(0, UUID.class);
        int numberOfTokens = event.getArgument(1, Integer.class);

        List<Token> tokens = generateTokens(cid, numberOfTokens);
        queue.publish(new Event(TokensReplied.topic, new Object[]{tokens}));
    }

    public List<Token> generateTokens(UUID cid, int numberOfTokens) {
        if (!tokenDatabaseOld.containsKey(cid)) {
            tokenDatabaseOld.put(cid, new ArrayList<>());
        }

        List<Token> currentTokensOfCustomer = tokenDatabaseOld.get(cid);

        if (currentTokensOfCustomer.size() <= 1) {
            //Create tokens
            System.out.println("Generating "+numberOfTokens+" new tokens");
            for (int i=0; i<numberOfTokens; i++ ) {
                Token token = new Token(cid);
                tokenDatabaseOld.get(cid).add(token);
            }
        }

        return tokenDatabaseOld.get(cid);
    }*/
}
