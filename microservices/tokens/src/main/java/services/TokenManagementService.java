package services;

import event.account.BankAccountIdFromCustomerIdRequested;
import event.token.TokensReplied;
import event.token.CustomerIdFromTokenRequested;
import event.token.TokensRequested;
import messaging.Event;
import messaging.MessageQueue;

import java.util.*;

import services.exceptions.CanNotGenerateTokensException;

public class TokenManagementService {

    UUID testCid = UUID.nameUUIDFromBytes(("cid-manyTokens").getBytes());
    UUID testToken1 = UUID.randomUUID();
    UUID testToken2 = UUID.randomUUID();

    public HashMap<UUID, List<UUID>> tokenDatabase = new HashMap<>()
    {{ put(testCid, Arrays.asList(testToken1,testToken2)); }};

    private final MessageQueue queue;

    public TokenManagementService(MessageQueue mq) {
        queue = mq;
        System.out.println("token management service running");
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
                                        findCustomerFromTokenId(request.getTokenId())
                                )
                        }
                )
        );
    }

    private UUID findCustomerFromTokenId(UUID tokenId) {

        System.out.println("Looking for token: "+tokenId);

        for (UUID cid : tokenDatabase.keySet()) {
            if (tokenDatabase.get(cid).contains(tokenId)) {
                invalidateToken(tokenId, cid);
                System.out.println("Customer: " + cid.toString());
                return cid;
            }
        }
        System.out.println("Customer is not found");
        return UUID.randomUUID(); //"Token not found"; //TODO: Actually handle the errors
    }

    private void invalidateToken(UUID tokenId, UUID cid) {
        List<UUID> tokenIds = tokenDatabase.get(cid);
        tokenIds.remove(tokenId);

        tokenDatabase.remove(cid);
        tokenDatabase.put(cid,tokenIds);
    }

    public void handleRequestTokens(Event event) {
        final var request = event.getArgument(0, TokensRequested.class);

        System.out.println("Handling event in token management: " + request.getCorrelationId());

        TokensReplied replyEvent;
        try {
            List<UUID> tokens = generateNewTokens(request.getCid(), request.getNoOfTokens());
            replyEvent = new TokensReplied(
                    request.getCorrelationId(),
                    new TokensReplied.TokensRepliedSuccess(tokens)
            );
        } catch (CanNotGenerateTokensException e) {
            replyEvent = new TokensReplied(
                    request.getCorrelationId(),
                    new TokensReplied.TokensRepliedFailure(
                            tokenDatabase.get(request.getCid()),
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

    public List<UUID> generateNewTokens(UUID cid, int numberOfTokens) throws CanNotGenerateTokensException {

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
                System.out.println("Generated: "+cid+" Token: "+newToken);
            }
        } else {
            String errorMessage = "Customer has "+currentTokensOfCustomer.size()+" already and can therefore not request tokens";
            throw new CanNotGenerateTokensException(errorMessage);
        }
        return tokenDatabase.get(cid);
    }
}
