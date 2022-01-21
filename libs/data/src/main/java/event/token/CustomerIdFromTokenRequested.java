package event.token;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

// @author : Naja
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CustomerIdFromTokenRequested extends BaseEvent {
    public static String topic = "customerId_from_token_requested";
    private UUID tokenId;

    public CustomerIdFromTokenRequested(UUID correlationId, UUID tokenId) {
        super(correlationId);
        this.tokenId = tokenId;
    }
}
