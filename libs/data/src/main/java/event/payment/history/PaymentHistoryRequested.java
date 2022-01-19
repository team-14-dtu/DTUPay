package event.payment.history;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PaymentHistoryRequested extends BaseEvent {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    public static class PaymentMerchantHistoryRequested extends BaseEvent {
        private UUID merchantId;

        public static String topic = "payment_merchant_history_requested";

        public PaymentMerchantHistoryRequested(UUID correlationId, UUID merchantId) {
            super(correlationId);
            this.merchantId = merchantId;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    public static class PaymentCustomerHistoryRequested extends BaseEvent {
        private UUID customerId;

        public static String topic = "payment_customer_history_requested";

        public PaymentCustomerHistoryRequested(UUID correlationId, UUID customerId) {
            super(correlationId);
            this.customerId = customerId;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    public static class PaymentManagerHistoryRequested extends BaseEvent {

        public static String topic = "payment_manager_history_requested";

        public PaymentManagerHistoryRequested(UUID correlationId) {
            super(correlationId);
        }
    }
}
