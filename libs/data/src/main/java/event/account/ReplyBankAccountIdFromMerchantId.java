package event.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReplyBankAccountIdFromMerchantId extends BaseEvent {
    private String bankAccountId;

    public ReplyBankAccountIdFromMerchantId(String correlationId, String bankAccountId) {
        super(correlationId);
        this.bankAccountId = bankAccountId;
    }

    public static String topic = "ReplyBankAccountIdFromMerchantId";
}
