package event.account;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyRetireUser {
    private String customerId;
    private Boolean success;

    public static String topic = "reply_retire_user";
}
