package event.payment.history;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rest.PaymentHistoryCustomer;
import rest.PaymentHistoryManager;
import team14messaging.BaseEvent;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PaymentManagerHistoryReplied extends BaseEvent {
    private List<PaymentHistoryManager> managerHistoryList;

    public static String topic = "payment_manager_history_replied";

    public PaymentManagerHistoryReplied(UUID correlationId, List<PaymentHistoryManager> managerHistoryList) {
        super(correlationId);
        this.managerHistoryList = managerHistoryList;
    }
}
