package event.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BankAccountIdFromMerchantIdReplied extends BaseEvent {
    private String bankAccountId;

    public BankAccountIdFromMerchantIdReplied(UUID correlationId, String bankAccountId) {
        super(correlationId);
        this.bankAccountId = bankAccountId;
    }

    public static String topic = "bankAccountId_from_merchantId_replied";
}
