package event.account;


import event.BaseReplyEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RegisterUserReplied extends BaseReplyEvent {
    public static String topic = "register_user_replied";
    private Success successResponse;
    private SimpleFailure failureResponse;

    public RegisterUserReplied(UUID correlationId, Success successResponse) {
        super(correlationId);
        this.successResponse = successResponse;
        this.failureResponse = null;
    }

    public RegisterUserReplied(UUID correlationId, SimpleFailure failResponse) {
        super(correlationId);
        this.successResponse = null;
        this.failureResponse = failResponse;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Success implements SuccessResponse {
        private String name;
        private String bankAccountId;
        private String cpr;
        private UUID customerId;
    }
}
