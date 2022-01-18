package event.payment.history;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rest.PaymentHistoryMerchant;
import team14messaging.BaseEvent;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PaymentMerchantHistoryReplied extends BaseEvent {
    private List<PaymentHistoryMerchant> merchantHistoryList;

    public static String topic = "payment_merchant_history_replied";

    public PaymentMerchantHistoryReplied(UUID correlationId, List<PaymentHistoryMerchant> merchantHistoryList) {
        super(correlationId);
        this.merchantHistoryList = merchantHistoryList;
    }
}
