package event.payment.pay;


import event.BaseReplyEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

// @author : Søren

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PayReplied extends BaseReplyEvent {
    public static String topic = "pay_replied";
    private PayRepliedSuccess successResponse;
    private PayRepliedFailure failureResponse;

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
    public static class PayRepliedFailure implements FailureResponse {
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
        private UUID id;
        private BigDecimal amount;
        private String description;
    }
}
