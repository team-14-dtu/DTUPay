package event.payment.history;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rest.User;
import team14messaging.BaseEvent;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PaymentHistoryRequested extends BaseEvent {
    private String userId;
    private User.Type userType;

    public static String topic = "payment_history_requested";

    public PaymentHistoryRequested(UUID correlationId, String bankAccountId, User.Type userType) {
        super(correlationId);
        this.userId = bankAccountId;
        this.userType = userType;
    }
}
