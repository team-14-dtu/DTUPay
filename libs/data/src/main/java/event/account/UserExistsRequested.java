package event.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserExistsRequested extends BaseEvent {
    public static String topic = "user_exists_requested";
    private UUID userId;

    public UserExistsRequested(UUID correlationId, UUID userId) {
        super(correlationId);
        this.userId = userId;
    }
}
