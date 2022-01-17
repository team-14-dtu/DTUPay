package event.payment.pay;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RequestPay extends BaseEvent {
    private String tokenId;
    private String merchantId;
    private BigDecimal amount;
    private String description;

    public static String topic = "pay_requested";

    public RequestPay(String correlationId, String tokenId, String merchantId, BigDecimal amount, String description) {
        super(correlationId);
        this.tokenId = tokenId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.description = description;
    }
}
