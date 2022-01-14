package rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Type userType;
    private String userName;
    private String cpr;
    private String accountId;
    private String userId;
    private List<Token> tokens;

    public enum Type {
        CUSTOMER,
        MERCHANT,
        MANAGER,
    }

    public User(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }
}

