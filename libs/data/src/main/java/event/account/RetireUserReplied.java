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
public class RetireUserReplied extends BaseEvent {
    private String customerId;
    private Boolean success;

    public static String topic = "retire_user_replied";
}
