package event.token;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TokensRequested extends BaseEvent {
    private String cid;
    private int noOfTokens;

    public TokensRequested(String correlationId, String cid, int noOfTokens) {
        super(correlationId);
        this.cid = cid;
        this.noOfTokens = noOfTokens;
    }

    public static String topic = "tokens_requested";
}