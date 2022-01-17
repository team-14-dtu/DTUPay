package event.account;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RequestRetireUser extends BaseEvent {
    private String cpr;

    public static String topic = "RequestRetireUser";

    public RequestRetireUser(String correlationId, String cpr) {
        super(correlationId);
        this.cpr = cpr;
    }
}
