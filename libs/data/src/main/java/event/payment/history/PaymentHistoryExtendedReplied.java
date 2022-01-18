package event.payment.history;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PaymentHistoryExtendedReplied extends BaseEvent {
    private List<PaymentHistoryReplied> historyList;

    public static String topic = "payment_history_extended_replied";

    public PaymentHistoryExtendedReplied(UUID correlationId, List<PaymentHistoryReplied> historyList) {
        super(correlationId);
        this.historyList = historyList;
    }
}
