package services.Merchant;

import rest.RegisterUser;
import rest.RetireUser;
import rest.User;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class AccountsMerchantClient extends MerchantClient {

    public User getResponse() {
        return webTarget.request().get(User.class);
    }

    public Response registerUser(String bankAccountId, String cprNumber, String name) {
        return webTarget.path("accounts").request().post(Entity.json(new RegisterUser(
                bankAccountId,
                name,
                cprNumber,
                true
        )));
    }

    public Response retireUser(String cpr) {
        return webTarget
                .path("accounts")
                .path("delete") // TODO make into delete
                .request().post(Entity.json(new RetireUser(
                        cpr
                )));
    }
}
