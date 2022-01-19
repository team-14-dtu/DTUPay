package services.entities;

import java.util.UUID;

public class User {
    public final UUID id;
    public final String bankAccountId;
    public final String name;
    public final String cpr;

    public User(UUID id, String bankAccountId, String name, String cpr) {
        this.id = id;
        this.bankAccountId = bankAccountId;
        this.name = name;
        this.cpr = cpr;
    }
}
