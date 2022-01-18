package event.token;

import event.payment.pay.PayRepliedFailure;
import event.payment.pay.PayRepliedSuccess;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rest.Token;
import team14messaging.BaseEvent;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TokensReplied extends BaseEvent {
    private TokensRepliedSuccess successResponse;
    private TokensRepliedFailure failResponse;

    public static String topic = "tokens_replied";

    public TokensReplied(UUID correlationId, TokensRepliedSuccess successResponse) {
        super(correlationId);
        this.successResponse = successResponse;
    }

    public TokensReplied(UUID correlationId, TokensRepliedFailure failResponse) {
        super(correlationId);
        this.failResponse = failResponse;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokensRepliedFailure {
        private List<UUID> tokens;
        private String message;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokensRepliedSuccess extends BaseEvent {
        private List<UUID> tokens;
    }


}

