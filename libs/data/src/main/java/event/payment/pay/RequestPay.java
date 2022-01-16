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
    private String id;
    private String customerId;
    private String merchantId;
    private BigDecimal amount;
    private String description;

    public static String topic = "request_pay";

    public RequestPay(String correlationId, String id, String customerId, String merchantId, BigDecimal amount, String description) {
        super(correlationId);
        this.id = id;
        this.customerId = customerId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.description = description;
    }
}
