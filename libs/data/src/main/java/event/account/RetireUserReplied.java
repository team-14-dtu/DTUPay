package event.account;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RetireUserReplied extends BaseEvent {
    private Boolean success;

    public RetireUserReplied(String correlationId, Boolean success) {
        super(correlationId);
        this.success = success;
    }

    public static String topic = "retire_user_replied";
}
