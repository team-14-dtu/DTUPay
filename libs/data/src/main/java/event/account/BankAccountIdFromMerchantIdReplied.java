package event.account;
// @author : Søren
import event.BaseReplyEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;    // @author : Naja

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BankAccountIdFromMerchantIdReplied extends BaseReplyEvent {

    public static String topic = "bankAccountId_from_merchantId_replied";
    private Success successResponse;
    private SimpleFailure failureResponse;

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
