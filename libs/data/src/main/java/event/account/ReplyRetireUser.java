package event.account;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyRetireUser extends BaseEvent {
    private Boolean success;

    public ReplyRetireUser(String correlationId, Boolean success) {
        super(correlationId);
        this.success = success;
    }

    public static String topic = "ReplyRetireUser";
}
