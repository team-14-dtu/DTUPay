package event.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BankAccountIdFromCustomerIdRequested extends BaseEvent {
    private BRSuccess successResponse;
    private BRFailure failResponse;
    private UUID customerId;

    public BankAccountIdFromCustomerIdRequested(UUID correlationId, UUID customerId, BRSuccess successResponse) {
        super(correlationId);
        this.customerId = customerId;
        this.successResponse = successResponse;
    }

    public BankAccountIdFromCustomerIdRequested(UUID correlationId, BRFailure failResponse) {
        super(correlationId);
        this.failResponse = failResponse;
    }

    public static String topic = "bankaccountId_from_customerId_requested";

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BRFailure {
        private String message;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BRSuccess extends BaseEvent {
        private UUID customerId;
    }
}
