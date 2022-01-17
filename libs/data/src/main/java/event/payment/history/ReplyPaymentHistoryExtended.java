package event.payment.history;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReplyPaymentHistoryExtended extends BaseEvent {
    private List<ReplyPaymentHistory> historyList;

    public static String topic = "payment_history_extended_replied";

    public ReplyPaymentHistoryExtended(String correlationId, List<ReplyPaymentHistory> historyList) {
        super(correlationId);
        this.historyList = historyList;
    }
}
