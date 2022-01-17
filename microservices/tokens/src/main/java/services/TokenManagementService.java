package services;

import event.account.BankAccountIdFromCustomerIdRequested;
import event.token.TokensReplied;
import event.token.CustomerIdFromTokenRequested;
import messaging.Event;
import messaging.MessageQueue;

import java.util.*;

import rest.Token;

public class TokenManagementService {
    UUID testCid = UUID.nameUUIDFromBytes(("cid-manyTokens").getBytes());
    Token testToken1 = new Token(testCid);
    Token testToken2 = new Token(testCid);

    UUID testTokenId = UUID.randomUUID();

    public HashMap<UUID, List<Token>> tokenDatabaseOld = new HashMap<>()
            {{ put(testCid, Arrays.asList(testToken1,testToken2)); }}; //TODO: migrate token generation to use new db (i.e. don't use token model)

    public HashMap<UUID, List<UUID>> tokenDatabase = new HashMap<>()
        {{ put(testCid, Arrays.asList(UUID.randomUUID(), testTokenId)); }};

    private final MessageQueue queue;

    public TokenManagementService(MessageQueue mq) {
        queue = mq;
        System.out.println("token management service running");
//        queue.addHandler(RequestTokens.getEventName(), this::generateTokensEvent);
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
                                        findCustomerFromTokenId(testTokenId.toString())//TODO: replace with findCustomerFromTokenId(request.getTokenId()) when gen-tokens are established
                                )
                        }
                )
        );
    }

    private String findCustomerFromTokenId(String tokenIdString) {
        UUID tokenId = UUID.fromString(tokenIdString);

        for (UUID cid : tokenDatabase.keySet()) {
            if (tokenDatabase.get(cid).contains(tokenId)) {
                //invalidateToken(tokenId, cid);
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


    public void generateTokensEvent(Event event) {
        System.out.println("test to see if message got consumed");
        UUID cid = event.getArgument(0, UUID.class);
        int numberOfTokens = event.getArgument(1, Integer.class);

        List<Token> tokens = generateTokens(cid, numberOfTokens);
        queue.publish(new Event(TokensReplied.getEventName(), new Object[]{tokens}));
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
    }
}
