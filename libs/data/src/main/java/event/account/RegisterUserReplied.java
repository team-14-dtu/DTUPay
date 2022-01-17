package event.account;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RegisterUserReplied extends BaseEvent {
    private String cpr;
    private RegisterUserRepliedSuccess successResponse;
    private RegisterUserRepliedFailure failResponse;

    public static String topic = "register_user_replied";

    public RegisterUserReplied(String correlationId, String cpr, RegisterUserRepliedSuccess successResponse, RegisterUserRepliedFailure failResponse) {
        super(correlationId);
        this.cpr = cpr;
        this.successResponse = successResponse;
        this.failResponse = failResponse;
    }
}
