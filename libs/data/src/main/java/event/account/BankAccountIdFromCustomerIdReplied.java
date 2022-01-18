package event.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BankAccountIdFromCustomerIdReplied extends BaseEvent {
    private UUID customerId;
    private String bankAccountId;

    public BankAccountIdFromCustomerIdReplied(UUID correlationId, UUID customerId, String bankAccountId) {
        super(correlationId);
        this.customerId = customerId;
        this.bankAccountId = bankAccountId;
    }

    public static String topic = "bankaccountId_from_customerId_replied";
}
