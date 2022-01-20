package event.token;

import event.BaseReplyEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TokensReplied extends BaseReplyEvent {
    private TokensReplied.Success successResponse;
    private TokensReplied.Failure failureResponse;

    public static String topic = "tokens_replied";

    public TokensReplied(UUID correlationId, Success successResponse) {
        super(correlationId);
        this.successResponse = successResponse;
        this.failureResponse = null;
    }

    public TokensReplied(UUID correlationId, Failure failureResponse) {
        super(correlationId);
        this.successResponse = null;
        this.failureResponse = failureResponse;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Success implements SuccessResponse {
        private List<UUID> tokens;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Failure implements FailureResponse {
        private List<UUID> tokens;
        private String reason;

        @Override
        public String getReason() {return this.reason;};
    }
}

