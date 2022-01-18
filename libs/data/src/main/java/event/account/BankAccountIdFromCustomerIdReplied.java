package event.account;

import event.BaseReplyEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BankAccountIdFromCustomerIdReplied extends BaseReplyEvent {

    private Success successResponse;
    private SimpleFailure failureResponse;


    public static String topic = "bankaccountId_from_customerId_replied";

    public BankAccountIdFromCustomerIdReplied(UUID correlationId, Success successResponse) {
        super(correlationId);
        this.successResponse = successResponse;
    }

    public BankAccountIdFromCustomerIdReplied(UUID correlationId, SimpleFailure failureResponse) {
        super(correlationId);
        this.failureResponse = failureResponse;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Success implements SuccessResponse {
        private UUID customerId;
        private String bankAccountId;
    }
}
