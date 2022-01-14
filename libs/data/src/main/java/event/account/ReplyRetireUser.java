package event.account;


import event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReplyRetireUser extends BaseEvent {
    private String customerId;
    private Boolean success;

    public static String topic = "reply_retire_user";
}
