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
public class ReplyRegisterUser extends BaseEvent {
    private String cpr;
    private ReplyRegisterUserSuccess successResponse;
    private ReplyRegisterUserFailure failResponse;

    public static String topic = "reply_register_user";
}
