package event.account;

import event.BaseReplyEvent;
import lombok.*;
import team14messaging.BaseEvent;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BankAccountIdFromMerchantIdReplied extends BaseReplyEvent {

    private Success successResponse;
    private SimpleFailure failureResponse;

    public static String topic = "bankAccountId_from_merchantId_replied";

    public BankAccountIdFromMerchantIdReplied(UUID correlationId, Success successResponse) {
        super(correlationId);
        this.successResponse = successResponse;
    }

    public BankAccountIdFromMerchantIdReplied(UUID correlationId, SimpleFailure failureResponse) {
        super(correlationId);
        this.failureResponse = failureResponse;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Success implements SuccessResponse {
        private String bankAccountId;
        private String merchantName;
    }
}
