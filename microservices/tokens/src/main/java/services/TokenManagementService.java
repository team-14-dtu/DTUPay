package services;

import event.account.RequestBankAccountIdFromCustomerId;
import event.token.ReplyTokens;
import event.token.RequestCustomerIdFromToken;
import event.token.RequestTokens;
import messaging.Event;
import messaging.MessageQueue;

import java.util.*;

import rest.Token;

public class TokenManagementService {
    UUID testCid = UUID.nameUUIDFromBytes(("cid-manyTokens").getBytes());
    Token testToken1 = new Token(testCid);
    Token testToken2 = new Token(testCid);

    public HashMap<UUID, List<Token>> tokenDatabase = new HashMap<>()
            {{ put(testCid, Arrays.asList(testToken1,testToken2)); }};

    private final MessageQueue queue;

    public TokenManagementService(MessageQueue mq) {
        queue = mq;
        System.out.println("token management service running");
//        queue.addHandler(RequestTokens.getEventName(), this::generateTokensEvent);
        queue.addHandler(RequestCustomerIdFromToken.topic, this::handleRequestCustomerIdFromToken);
    }

    private void handleRequestCustomerIdFromToken(Event event) {
        final RequestCustomerIdFromToken request =
                event.getArgument(0, RequestCustomerIdFromToken.class);

        System.out.println("Handling event in token management: " + request.getCorrelationId());

        queue.publish(
                new Event(
                        RequestBankAccountIdFromCustomerId.topic,
                        new Object[]{
                                new RequestBankAccountIdFromCustomerId(
                                        request.getCorrelationId(),
                                        "1537eac8-a38f-4c69-b0a8-9bcf541da23f"
                                )
                        }
                )
        );
    }


    public void generateTokensEvent(Event event) {
        System.out.println("test to see if message got consumed");
        UUID cid = event.getArgument(0, UUID.class);
        int numberOfTokens = event.getArgument(1, Integer.class);

        List<Token> tokens = generateTokens(cid, numberOfTokens);
        queue.publish(new Event(ReplyTokens.getEventName(), new Object[]{tokens}));
    }

    public List<Token> generateTokens(UUID cid, int numberOfTokens) {
        if (!tokenDatabase.containsKey(cid)) {
            tokenDatabase.put(cid, new ArrayList<>());
        }

        List<Token> currentTokensOfCustomer = tokenDatabase.get(cid);

        if (currentTokensOfCustomer.size() <= 1) {
            //Create tokens
            System.out.println("Generating "+numberOfTokens+" new tokens");
            for (int i=0; i<numberOfTokens; i++ ) {
                Token token = new Token(cid);
                tokenDatabase.get(cid).add(token);
            }
        }

        return tokenDatabase.get(cid);
    }
}
