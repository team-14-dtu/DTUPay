package event.payment.history;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rest.PaymentHistoryCustomer;
import team14messaging.BaseEvent;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PaymentCustomerHistoryReplied extends BaseEvent {
    private List<PaymentHistoryCustomer> customerHistoryList;

    public static String topic = "payment_customer_history_replied";

    public PaymentCustomerHistoryReplied(UUID correlationId, List<PaymentHistoryCustomer> customerHistoryList) {
        super(correlationId);
        this.customerHistoryList = customerHistoryList;
    }
}
