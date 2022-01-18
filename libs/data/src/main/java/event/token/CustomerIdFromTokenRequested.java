package event.token;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CustomerIdFromTokenRequested extends BaseEvent {
    private UUID tokenId;

    public CustomerIdFromTokenRequested(UUID correlationId, UUID tokenId) {
        super(correlationId);
        this.tokenId = tokenId;
    }

    public static String topic = "customerId_from_token_requested";
}
