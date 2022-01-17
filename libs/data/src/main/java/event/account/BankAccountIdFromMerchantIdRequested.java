package event.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BankAccountIdFromMerchantIdRequested extends BaseEvent {
    private String merchantId;

    public BankAccountIdFromMerchantIdRequested(String correlationId, String merchantId) {
        super(correlationId);
        this.merchantId = merchantId;
    }

    public static String topic = "bankaccountId_from_merchantId_requested";
}
