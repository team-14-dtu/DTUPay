package event.account;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestRetireUser {
    private String customerId;

    public static String topic = "request_retire_user";
}
