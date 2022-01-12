package event.payments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePayment {
    private String name;

    public static String getEventName() {
        return "createPaymentEvent";
    }
}