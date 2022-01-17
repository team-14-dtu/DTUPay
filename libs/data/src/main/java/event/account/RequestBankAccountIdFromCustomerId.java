package event.account;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team14messaging.BaseEvent;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RequestBankAccountIdFromCustomerId extends BaseEvent {
    private String customerId;

    public RequestBankAccountIdFromCustomerId(String correlationId, String customerId) {
        super(correlationId);
        this.customerId = customerId;
    }

    public static String topic = "RequestBankAccountIdFromCustomerId";
}
