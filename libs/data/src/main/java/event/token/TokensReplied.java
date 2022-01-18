package event.token;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rest.Token;
import team14messaging.BaseEvent;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TokensReplied extends BaseEvent {
    private List<UUID> tokens;

    public TokensReplied(UUID correlationId, List<UUID> tokens) {
        super(correlationId);
        this.tokens = tokens;
    }

    public static String topic = "tokens_replied";


}