package event.account;
// @author : Petr
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RetireUserRequested extends BaseEvent {
    public static String topic = "retire_user_requested";
    private String cpr;

    public RetireUserRequested(UUID correlationId, String cpr) {
        super(correlationId);
        this.cpr = cpr;
    }
}
