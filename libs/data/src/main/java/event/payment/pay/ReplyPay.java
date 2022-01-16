package event.payment.pay;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReplyPay extends BaseEvent {
    private String id;
    private ReplyPaySuccess successResponse;
    private ReplyPayFailure failResponse;

    public static String topic = "reply_pay";

    public ReplyPay(String correlationId, String id, ReplyPaySuccess successResponse, ReplyPayFailure failResponse) {
        super(correlationId);
        this.id = id;
        this.successResponse = successResponse;
        this.failResponse = failResponse;
    }
}
