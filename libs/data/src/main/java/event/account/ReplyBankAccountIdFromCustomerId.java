package event.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReplyBankAccountIdFromCustomerId extends BaseEvent {
    private String customerId;
    private String bankAccountId;

    public ReplyBankAccountIdFromCustomerId(String correlationId, String customerId, String bankAccountId) {
        super(correlationId);
        this.customerId = customerId;
        this.bankAccountId = bankAccountId;
    }

    public static String topic = "bankaccountId_from_customerId_replied";
}
