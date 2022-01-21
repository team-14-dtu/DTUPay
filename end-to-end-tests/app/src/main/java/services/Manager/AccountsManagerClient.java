package services.Manager;

import rest.RegisterUser;
import rest.RetireUser;
import rest.User;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class AccountsManagerClient extends ManagerClient {

    public User getResponse() {
        return webTarget.request().get(User.class);
    }

    public Response registerUser(String bankAccountId, String cprNumber, String name, Boolean isMerchant) {
        return webTarget.path("accounts").request().post(Entity.json(new RegisterUser(
                bankAccountId,
                name,
                cprNumber,
                isMerchant
        )));
    }

    public Response retireUser(String cpr) {
        return webTarget
                .path("accounts")
                .path(cpr)
                .request().delete();
    }
}
