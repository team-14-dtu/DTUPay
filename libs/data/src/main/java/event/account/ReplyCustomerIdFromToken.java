package event.account;

public class ReplyCustomerIdFromToken {
    public final String customerId;
    public final String token;

    public ReplyCustomerIdFromToken(String customerId, String token) {
        this.customerId = customerId;
        this.token = token;
    }
}
