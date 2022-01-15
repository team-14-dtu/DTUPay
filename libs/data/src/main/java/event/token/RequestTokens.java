package event.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestTokens {
    public UUID customerId;
    public int numberOfTokens;

    public static String getEventName() {
        return "requestTokensEvent";
    }
}