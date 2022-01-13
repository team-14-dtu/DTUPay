package event.account;

public class ReplyBankAccountIdFromCustomerId {
    public final String bankAccountId;
    public final String customerId;

    public ReplyBankAccountIdFromCustomerId(String bankAccountId, String customerId) {
        this.bankAccountId = bankAccountId;
        this.customerId = customerId;
    }
}
