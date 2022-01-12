package event.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestTokens {
    public String customerId;
    public int numberOfTokens;

    public static String getEventName() {
        return "requestTokensEvent";
    }
}