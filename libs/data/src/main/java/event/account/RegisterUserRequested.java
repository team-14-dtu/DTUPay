package event.account;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RegisterUserRequested extends BaseEvent {
    private String name;
    private String bankAccountId;
    private String cpr;
    private Boolean isMerchant;

    public static String topic = "register_user_requested";

    public RegisterUserRequested(String correlationId, String name, String bankAccountId, String cpr, Boolean isMerchant) {
        super(correlationId);
        this.name = name;
        this.bankAccountId = bankAccountId;
        this.cpr = cpr;
        this.isMerchant = isMerchant;
    }
}
