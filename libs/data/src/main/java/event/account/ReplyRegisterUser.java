package event.account;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReplyRegisterUser extends BaseEvent {
    private String cpr;
    private ReplyRegisterUserSuccess successResponse;
    private ReplyRegisterUserFailure failResponse;

    public static String topic = "register_user_replied";

    public ReplyRegisterUser(String correlationId, String cpr, ReplyRegisterUserSuccess successResponse, ReplyRegisterUserFailure failResponse) {
        super(correlationId);
        this.cpr = cpr;
        this.successResponse = successResponse;
        this.failResponse = failResponse;
    }
}
