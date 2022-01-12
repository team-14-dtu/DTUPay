package event.account;

public class RequestRegisterUser {
    public final String name;
    public final String bankAccountId;
    public final String cpr;

    public RequestRegisterUser(String name, String bankAccountId, String cpr) {
        this.name = name;
        this.bankAccountId = bankAccountId;
        this.cpr = cpr;
    }
}
