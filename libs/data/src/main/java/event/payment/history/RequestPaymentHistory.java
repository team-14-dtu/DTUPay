package event.payment.history;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rest.User;
import team14messaging.BaseEvent;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RequestPaymentHistory extends BaseEvent {
    private String userId;
    private User.Type userType;

    public static String topic = "request_payment_history";

    public RequestPaymentHistory(String correlationId, String bankAccountId, User.Type userType) {
        super(correlationId);
        this.userId = bankAccountId;
        this.userType = userType;
    }
}
