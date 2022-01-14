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
public class RequestRegisterUser extends BaseEvent {
    private String name;
    private String bankAccountId;
    private String cpr;
    private Boolean isMerchant;

    public static String topic = "request_register_user";
}
