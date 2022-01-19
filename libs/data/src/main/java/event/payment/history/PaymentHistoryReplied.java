package event.payment.history;

import event.BaseReplyEvent;
import event.payment.pay.PayReplied;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rest.PaymentHistoryCustomer;
import rest.PaymentHistoryManager;
import rest.PaymentHistoryMerchant;
import team14messaging.BaseEvent;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode()
@NoArgsConstructor
public class PaymentHistoryReplied {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class PaymentMerchantHistoryReplied extends BaseReplyEvent {
        private PaymentMerchantHistoryRepliedSuccess successResponse;
        private PaymentMerchantHistoryRepliedFailure failureResponse;


        public static String topic = "payment_merchant_history_replied";


        public PaymentMerchantHistoryReplied(UUID correlationId, PaymentMerchantHistoryRepliedSuccess paymentMerchantHistoryRepliedSuccess) {
            super(correlationId);
            this.successResponse = paymentMerchantHistoryRepliedSuccess;
        }

        public PaymentMerchantHistoryReplied(UUID correlationId, PaymentMerchantHistoryRepliedFailure paymentMerchantHistoryRepliedFailure) {
            super(correlationId);
            this.failureResponse = paymentMerchantHistoryRepliedFailure;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PaymentMerchantHistoryRepliedFailure implements BaseReplyEvent.FailureResponse {
            private String message;


            @Override
            public String getReason() {
                return message;
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PaymentMerchantHistoryRepliedSuccess implements BaseReplyEvent.SuccessResponse {
            private List<PaymentHistoryMerchant> merchantHistoryList;
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class PaymentCustomerHistoryReplied extends BaseReplyEvent {
        private PaymentCustomerHistoryRepliedSuccess successResponse;
        private PaymentCustomerHistoryRepliedFailure failureResponse;


        public static String topic = "payment_customer_history_replied";

        public PaymentCustomerHistoryReplied(UUID correlationId, PaymentCustomerHistoryRepliedFailure paymentCustomerHistoryRepliedFailure) {
            super(correlationId);
            this.failureResponse = paymentCustomerHistoryRepliedFailure;
        }

        public PaymentCustomerHistoryReplied(UUID correlationId, PaymentCustomerHistoryRepliedSuccess paymentCustomerHistoryRepliedSuccess) {
            super(correlationId);
            this.successResponse = paymentCustomerHistoryRepliedSuccess;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PaymentCustomerHistoryRepliedFailure implements BaseReplyEvent.FailureResponse {
            private String message;


            @Override
            public String getReason() {
                return message;
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PaymentCustomerHistoryRepliedSuccess implements BaseReplyEvent.SuccessResponse {
            private List<PaymentHistoryCustomer> customerHistoryList;
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class PaymentManagerHistoryReplied extends BaseReplyEvent {
        private PaymentManagerHistoryRepliedSuccess successResponse;
        private PaymentManagerHistoryRepliedFailure failureResponse;


        public static String topic = "payment_manager_history_replied";

        public PaymentManagerHistoryReplied(UUID correlationId, PaymentManagerHistoryReplied.PaymentManagerHistoryRepliedSuccess paymentManagerHistoryRepliedSuccess) {
            super(correlationId);
            this.successResponse = paymentManagerHistoryRepliedSuccess;
        }

        public PaymentManagerHistoryReplied(UUID correlationId, PaymentManagerHistoryReplied.PaymentManagerHistoryRepliedFailure paymentManagerHistoryRepliedFailure) {
            super(correlationId);
            this.failureResponse = paymentManagerHistoryRepliedFailure;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PaymentManagerHistoryRepliedFailure implements BaseReplyEvent.FailureResponse {
            private String message;


            @Override
            public String getReason() {
                return message;
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PaymentManagerHistoryRepliedSuccess implements BaseReplyEvent.SuccessResponse {
            private List<PaymentHistoryManager> managerHistoryList;
        }


    }
}
