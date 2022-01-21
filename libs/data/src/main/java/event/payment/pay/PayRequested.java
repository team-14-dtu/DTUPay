package event.payment.pay;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PayRequested extends BaseEvent {
    public static String topic = "pay_requested";
    private UUID tokenId;
    private UUID merchantId;
    private BigDecimal amount;
    private String description;

    public PayRequested(UUID correlationId, UUID tokenId, UUID merchantId, BigDecimal amount, String description) {
        super(correlationId);
        this.tokenId = tokenId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.description = description;
    }
}
