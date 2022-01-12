package event.account;

public class RequestRegisterUser {
    public final String name;
    public final String bankAccountId;
    public final String cpr;
    public final Boolean isMerchant;

    public RequestRegisterUser(String name, String bankAccountId, String cpr, Boolean isMerchant) {
        this.name = name;
        this.bankAccountId = bankAccountId;
        this.cpr = cpr;
        this.isMerchant = isMerchant;
    }
}
