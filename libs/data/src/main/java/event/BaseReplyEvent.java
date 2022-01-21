package event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

@NoArgsConstructor
public abstract class BaseReplyEvent extends BaseEvent {

    public BaseReplyEvent(UUID correlationId) {
        super(correlationId);
    }

    public boolean isSuccess() {
        return getSuccessResponse() != null;
    }

    public abstract SuccessResponse getSuccessResponse();

    public abstract FailureResponse getFailureResponse();

    public interface SuccessResponse {
    }

    public interface FailureResponse {
        public String getReason();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleFailure implements FailureResponse {
        private String reason;
    }
}
