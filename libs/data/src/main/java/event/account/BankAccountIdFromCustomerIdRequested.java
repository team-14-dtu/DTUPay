package event.account;
// @author : David
import event.BaseReplyEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BankAccountIdFromCustomerIdRequested extends BaseReplyEvent {
    public static String topic = "bankaccountId_from_customerId_requested";
    private BRSuccess successResponse;
    private BRFailure failureResponse;

    public BankAccountIdFromCustomerIdRequested(UUID correlationId, BRSuccess successResponse) {
        super(correlationId);
        this.successResponse = successResponse;
    }

    public BankAccountIdFromCustomerIdRequested(UUID correlationId, BRFailure failResponse) {
        super(correlationId);
        this.failureResponse = failResponse;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BRFailure implements FailureResponse {
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BRSuccess implements SuccessResponse {
        private UUID customerId;
    }
}
