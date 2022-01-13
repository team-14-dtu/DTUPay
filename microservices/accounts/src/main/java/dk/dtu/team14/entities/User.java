package dk.dtu.team14.entities;

public class User {
    public final String id;
    public final String bankAccountId;
    public final String name;
    public final String cpr;

    public User(String id, String bankAccountId, String name, String cpr) {
        this.id = id;
        this.bankAccountId = bankAccountId;
        this.name = name;
        this.cpr = cpr;
    }
}
