package event.account;

public class RequestBankAccountIdFromCustomerId {
    public final String customerId;

    public RequestBankAccountIdFromCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
