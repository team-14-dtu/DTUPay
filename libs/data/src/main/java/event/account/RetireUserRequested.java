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
public class RetireUserRequested extends BaseEvent {
    private String customerId;

    public static String topic = "retire_user_requested";
}
