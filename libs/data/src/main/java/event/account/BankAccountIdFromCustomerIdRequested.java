package event.account;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BankAccountIdFromCustomerIdRequested extends BaseEvent {
    private UUID customerId;

    public BankAccountIdFromCustomerIdRequested(UUID correlationId, UUID customerId) {
        super(correlationId);
        this.customerId = customerId;
    }

    public static String topic = "bankaccountId_from_customerId_requested";
}
