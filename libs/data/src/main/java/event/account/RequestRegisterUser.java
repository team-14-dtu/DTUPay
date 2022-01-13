package event.account;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestRegisterUser {
    private String name;
    private String bankAccountId;
    private String cpr;
    private Boolean isMerchant;

    public static String topic = "request_register_user";
}
