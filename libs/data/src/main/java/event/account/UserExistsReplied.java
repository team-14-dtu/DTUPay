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
public class UserExistsReplied extends BaseReplyEvent {

    private Success successResponse;
    private SimpleFailure failureResponse;

    public static String topic = "user_exists_replied";

    public UserExistsReplied(UUID correlationId, Success successResponse) {
        super(correlationId);
        this.successResponse = successResponse;
    }

    public UserExistsReplied(UUID correlationId, SimpleFailure failureResponse) {
        super(correlationId);
        this.failureResponse = failureResponse;
    }

    @Data
    @NoArgsConstructor
    public static class Success implements SuccessResponse {}
}
