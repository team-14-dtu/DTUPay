package event.account;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyRegisterUserSuccess implements Response {
    private String name;
    private String bankAccountId;
    private String cpr;
    private String customerId;
}