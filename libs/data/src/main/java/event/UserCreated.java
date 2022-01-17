package event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreated {
    private String firstname;
    private String lastname;
    private String id;

    public static String getEventName() {
        return "user_created_replied";
    }
}
