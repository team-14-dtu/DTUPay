package services;

import event.token.ReplyTokens;
import event.token.RequestTokens;
import messaging.Event;
import messaging.MessageQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import rest.Token;

public class TokenManagementService {
    public HashMap<String, List<Token>> tokenDatabase = new HashMap<>();
    private MessageQueue queue;

    public TokenManagementService(MessageQueue mq) {
        queue = mq;
        System.out.println("token management service running");
        queue.addHandler(RequestTokens.getEventName(), this::generateTokensEvent);
    }

    public void generateTokensEvent(Event event) {
        System.out.println("test to see if message got consumed");
        String cid = event.getArgument(0, String.class);
        int numberOfTokens = event.getArgument(1, Integer.class);

        List<Token> tokens = generateTokens(cid, numberOfTokens);
        queue.publish(new Event(ReplyTokens.getEventName(), new Object[]{tokens}));
    }

    public List<Token> generateTokens(String cid, int numberOfTokens) {
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
        } else {
            //post error
        }
        return tokenDatabase.get(cid);
    }


    /*public TokenManagementService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("requestTokensEvent", this::handleTokensRequested);
    }

    public void handleTokensRequested(MessageQueue mq) {
        queue = mq;
        queue.addHandler(RequestTokens.getEventName(), this::handleTokensReply);
    }

    public List<Token> generateTokens(String cid, int numberOfTokens) {
        List<Token> currentTokensOfCustomer = tokenDatabase.get(cid); //returns null if cid does not exist
        if (currentTokensOfCustomer.size() <= 1) {
            //Create tokens
            for (int i=0; i>numberOfTokens; i++ ) {
                Token token = new Token(cid);
                tokenDatabase.get(cid).add(token);

            }
        } else {
            //post error
        }
        return listOfTokens.join();
    }

    public void handleTokensReply(Event ev) {
        ReplyTokens tokens = ev.getArgument(0, ReplyTokens.class);
        listOfTokens.complete(tokens);
    }*/

}
