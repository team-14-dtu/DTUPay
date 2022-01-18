package event.payment.pay;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PayReplied extends BaseEvent {
    private PayRepliedSuccess successResponse;
    private PayRepliedFailure failResponse;

    public static String topic = "pay_replied";

    public PayReplied(UUID correlationId, PayRepliedSuccess successResponse, PayRepliedFailure failResponse) {
        super(correlationId);
        this.successResponse = successResponse;
        this.failResponse = failResponse;
    }


}
