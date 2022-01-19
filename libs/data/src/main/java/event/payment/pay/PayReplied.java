package event.payment.pay;


import event.BaseReplyEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PayReplied extends BaseReplyEvent {
    private PayRepliedSuccess successResponse;
    private PayRepliedFailure failureResponse;

    public static String topic = "pay_replied";

    public PayReplied(UUID correlationId, PayRepliedSuccess successResponse) {
        super(correlationId);
        this.successResponse = successResponse;
    }

    public PayReplied(UUID correlationId, PayRepliedFailure failResponse) {
        super(correlationId);
        this.failureResponse = failResponse;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayRepliedFailure implements FailureResponse{
        private String message;


        @Override
        public String getReason() {
            return message;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayRepliedSuccess implements SuccessResponse {
        private String id;
        private BigDecimal amount;
        private String description;
    }
}
