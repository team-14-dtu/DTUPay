package event.token;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TokensRequested extends BaseEvent {
    private UUID cid;
    private int noOfTokens;

    public TokensRequested(UUID correlationId, UUID cid, int noOfTokens) {
        super(correlationId);
        this.cid = cid;
        this.noOfTokens = noOfTokens;
    }

    public static String topic = "tokens_requested";
}