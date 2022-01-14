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
public class RequestRetireUser extends BaseEvent {
    private String customerId;

    public static String topic = "request_retire_user";
}
