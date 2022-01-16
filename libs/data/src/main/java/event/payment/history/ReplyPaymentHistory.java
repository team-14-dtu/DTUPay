package event.payment.history;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode()
@NoArgsConstructor
public class ReplyPaymentHistory {
    private String id;
    private BigDecimal amount;
    private String description;

    public static String topic = "reply_payment_history";

    public ReplyPaymentHistory(String id, BigDecimal amount, String description) {
        this.id = id;
        this.amount = amount;
        this.description = description;
    }
}
