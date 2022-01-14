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
public class ReplyRegisterUser extends BaseEvent {
    private String cpr;
    private ReplyRegisterUserSuccess successResponse;
    private ReplyRegisterUserFailure failResponse;

    public static String topic = "reply_register_user";
}
