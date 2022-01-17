package event.account;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RetireUserRequested extends BaseEvent {
    private String cpr;

    public static String topic = "retire_user_requested";

    public RetireUserRequested(String correlationId, String cpr) {
        super(correlationId);
        this.cpr = cpr;
    }
}
