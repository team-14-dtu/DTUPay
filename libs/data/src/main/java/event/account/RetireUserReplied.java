package event.account;


import event.BaseReplyEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RetireUserReplied extends BaseReplyEvent {

    private Success successResponse;
    private SimpleFailure failureResponse;

    public static String topic = "retire_user_replied";

    public RetireUserReplied(UUID correlationId, Success successResponse) {
        super(correlationId);
        this.successResponse = successResponse;
    }

    public RetireUserReplied(UUID correlationId, SimpleFailure failureResponse) {
        super(correlationId);
        this.failureResponse = failureResponse;
    }

    @Data
    public static class Success implements SuccessResponse {
    }
}
