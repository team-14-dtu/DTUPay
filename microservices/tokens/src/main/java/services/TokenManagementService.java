package services;

import event.token.ReplyTokens;
import event.token.RequestTokens;
import messaging.Event;
import messaging.MessageQueue;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import rest.Token;

public class TokenManagementService {
    MessageQueue queue;
    private CompletableFuture<ReplyTokens> listOfTokens;
    HashMap<String, List<Token>> tokenDatabase = new HashMap<>();

    public TokenManagementService(MessageQueue q) {
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
    }

}
