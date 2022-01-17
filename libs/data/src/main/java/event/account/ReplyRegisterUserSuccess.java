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
public class ReplyRegisterUserSuccess extends BaseEvent {
    private String name;
    private String bankAccountId;
    private String cpr;
    private String customerId;
}
