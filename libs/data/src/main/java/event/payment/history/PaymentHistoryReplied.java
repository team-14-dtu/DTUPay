package event.payment.history;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rest.PaymentHistoryCustomer;
import rest.PaymentHistoryManager;
import rest.PaymentHistoryMerchant;
import team14messaging.BaseEvent;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PaymentHistoryReplied extends BaseEvent {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class PaymentMerchantHistoryReplied extends BaseEvent {
        private List<PaymentHistoryMerchant> merchantHistoryList;

        public static String topic = "payment_merchant_history_replied";

        public PaymentMerchantHistoryReplied(UUID correlationId, List<PaymentHistoryMerchant> merchantHistoryList) {
            super(correlationId);
            this.merchantHistoryList = merchantHistoryList;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class PaymentCustomerHistoryReplied extends BaseEvent {
        private List<PaymentHistoryCustomer> customerHistoryList;

        public static String topic = "payment_customer_history_replied";

        public PaymentCustomerHistoryReplied(UUID correlationId, List<PaymentHistoryCustomer> customerHistoryList) {
            super(correlationId);
            this.customerHistoryList = customerHistoryList;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class PaymentManagerHistoryReplied extends BaseEvent {
        private List<PaymentHistoryManager> managerHistoryList;

        public static String topic = "payment_manager_history_replied";

        public PaymentManagerHistoryReplied(UUID correlationId, List<PaymentHistoryManager> managerHistoryList) {
            super(correlationId);
            this.managerHistoryList = managerHistoryList;
        }
    }
}
