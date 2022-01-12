package event.token;

import rest.Token;
import java.util.List;

public class ReplyTokens {
    public final List<Token> tokens;

    public ReplyTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public static String getEventName() {
        return "replyTokensEvent";
    }
}