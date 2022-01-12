package event.account;

interface Response {}

class Success implements Response {
    public final String name;
    public final String bankAccountId;
    public final String cpr;
    public final String customerId;

    public Success(String name, String bankAccountId, String cpr, String customerId) {
        this.name = name;
        this.bankAccountId = bankAccountId;
        this.cpr = cpr;
        this.customerId = customerId;
    }
}

class Failure implements Response {
    public final String message;

    Failure(String message) {
        this.message = message;
    }
}

public class ReplyRegisterUser {
    final Response response;

    public ReplyRegisterUser(Response response) {
        this.response = response;
    }
}
