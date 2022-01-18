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
public class PaymentManagerHistoryRequested extends BaseEvent {

    public static String topic = "payment_manager_history_requested";

    public PaymentManagerHistoryRequested(UUID correlationId) {
        super(correlationId);
    }
}
