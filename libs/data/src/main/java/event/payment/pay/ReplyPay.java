package event.payment.pay;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReplyPay extends BaseEvent {
    private ReplyPaySuccess successResponse;
    private ReplyPayFailure failResponse;

    public static String topic = "ReplyPay";

    public ReplyPay(String correlationId, ReplyPaySuccess successResponse, ReplyPayFailure failResponse) {
        super(correlationId);
        this.successResponse = successResponse;
        this.failResponse = failResponse;
    }


}
