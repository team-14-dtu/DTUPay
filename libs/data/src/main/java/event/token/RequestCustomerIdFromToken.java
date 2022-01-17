package event.token;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RequestCustomerIdFromToken extends BaseEvent {
    private String tokenId;

    public RequestCustomerIdFromToken(String correlationId, String tokenId) {
        super(correlationId);
        this.tokenId = tokenId;
    }

    public static String topic = "customerId_from_token_requested";
}
