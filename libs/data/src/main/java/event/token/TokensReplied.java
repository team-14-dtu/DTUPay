package event.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rest.Token;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokensReplied {
    private List<Token> tokens;

    public static String getEventName() {
        return "tokens_replied";
    }
}