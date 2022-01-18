package event.payment.history;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PaymentMerchantHistoryRequested extends BaseEvent {
    private UUID merchantId;

    public static String topic = "payment_merchant_history_requested";

    public PaymentMerchantHistoryRequested(UUID correlationId, UUID merchantId) {
        super(correlationId);
        this.merchantId = merchantId;
    }
}
