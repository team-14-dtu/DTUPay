package event.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BankAccountIdFromMerchantIdRequested extends BaseEvent {
    private UUID merchantId;

    public BankAccountIdFromMerchantIdRequested(UUID correlationId, UUID merchantId) {
        super(correlationId);
        this.merchantId = merchantId;
    }

    public static String topic = "bankaccountId_from_merchantId_requested";
}
