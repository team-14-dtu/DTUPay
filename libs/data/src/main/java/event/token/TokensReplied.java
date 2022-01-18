package event.token;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rest.Token;
import team14messaging.BaseEvent;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TokensReplied extends BaseEvent {
    private List<String> tokens;

    public TokensReplied(String correlationId, List<String> tokens) {
        super(correlationId);
        this.tokens = tokens;
    }

    public static String topic = "tokens_replied";
}