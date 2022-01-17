package event.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BankAccountIdFromCustomerIdReplied extends BaseEvent {
    private String customerId;
    private String bankAccountId;

    public BankAccountIdFromCustomerIdReplied(String correlationId, String customerId, String bankAccountId) {
        super(correlationId);
        this.customerId = customerId;
        this.bankAccountId = bankAccountId;
    }

    public static String topic = "bankaccountId_from_customerId_replied";
}
