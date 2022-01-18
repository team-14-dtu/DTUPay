package event.payment.history;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PaymentCustomerHistoryRequested extends BaseEvent {
    private UUID customerId;

    public static String topic = "payment_customer_history_requested";

    public PaymentCustomerHistoryRequested(UUID correlationId, UUID customerId) {
        super(correlationId);
        this.customerId = customerId;
    }
}
