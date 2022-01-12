package event.payments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rest.Payment;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreated {
    private Payment payment;

    public static String getEventName() {
        return "paymentCreatedEvent";
    }
}
