package event.payments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rest.Payment;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPayments {
    private List<Payment> payments;

    public static String getEventName() {
        return "getPaymentEvent";
    }
}