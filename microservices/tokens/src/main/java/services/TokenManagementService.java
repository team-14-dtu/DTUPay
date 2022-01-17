package services;

import event.token.ReplyTokens;
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
    private MessageQueue queue;

    public TokenManagementService(MessageQueue mq) {
        //Add test data
        /*System.out.println("Adding test data for token management tests");
        String cidString = "cid-manyTokens";
        UUID testCid = UUID.nameUUIDFromBytes(cidString.getBytes());
        if (!tokenDatabase.containsKey(testCid)) {
            tokenDatabase.put(testCid, new ArrayList<>());
        }
        for (int i=0; i<2; i++ ) {
            Token token = new Token(testCid);
            tokenDatabase.get(testCid).add(token);
        }*/

        queue = mq;
        System.out.println("token management service running");
        queue.addHandler(RequestTokens.getEventName(), this::generateTokensEvent);
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
