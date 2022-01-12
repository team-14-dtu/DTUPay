import event.token.ReplyTokens;
import messaging.Event;
import messaging.MessageQueue;

import java.util.HashMap;
import java.util.List;

import rest.Token;

public class TokenManagementService {
    MessageQueue queue;
    HashMap<String, List<Token>> tokenDatabase = new HashMap<>();

    public TokenManagementService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("requestTokensEvent", this::handleTokensRequested);
    }

    public void handleTokensRequested(Event ev) {
        String cid = ev.getArgument(0, String.class);
        int numberOfTokens = ev.getArgument(1, Integer.class);

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

        /*var s = ev.getArgument(0, Student.class);
        s.setId("123");
        Event event = new Event("StudentIdAssigned", new Object[] { s });
        queue.publish(event);*/
        ReplyTokens replyTokensEvent = new ReplyTokens(tokenDatabase.get(cid));

        Event event = new Event(replyTokensEvent.getEventName(), null);
        queue.publish(event);
    }
}
