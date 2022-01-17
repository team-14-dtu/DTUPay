package rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Type userType;
    private String userName;
    private String cpr;
    private String accountId;
    private UUID userId;
    private List<Token> tokens;

    public enum Type {
        CUSTOMER,
        MERCHANT,
        MANAGER,
    }

    public User(String userName, UUID userId) {
        this.userName = userName;
        this.userId = userId;
    }
}

