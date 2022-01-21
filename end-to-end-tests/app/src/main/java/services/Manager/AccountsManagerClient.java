package services.Manager;

import rest.RegisterUser;
import rest.User;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class AccountsManagerClient extends ManagerClient {
    // @author : Petr
    public User getResponse() {
        return webTarget.request().get(User.class);
    }
    // @author : Emmanuel
    public Response registerUser(String bankAccountId, String cprNumber, String name, Boolean isMerchant) {
        return webTarget.path("accounts").request().post(Entity.json(new RegisterUser(
                bankAccountId,
                name,
                cprNumber,
                isMerchant
        )));
    }
    // @author : Petr
    public Response retireUser(String cpr) {
        return webTarget
                .path("accounts")
                .path(cpr)
                .request().delete();
    }
}
