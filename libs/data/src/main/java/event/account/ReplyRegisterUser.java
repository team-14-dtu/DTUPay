package event.account;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyRegisterUser {
    private String cpr;
    private ReplyRegisterUserSuccess successResponse;
    private ReplyRegisterUserFailure failResponse;

    public static String topic = "reply_register_user";
}
