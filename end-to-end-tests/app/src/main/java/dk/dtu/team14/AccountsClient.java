package dk.dtu.team14;

import rest.RegisterUser;
import rest.RetireUser;
import rest.User;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.UUID;

public class AccountsClient extends Client {

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

    public String retireUser(String cpr) {
        return webTarget
                .path("accounts")
                .path("delete") // TODO make into delete
                .request().post(Entity.json(new RetireUser(
                cpr
        ))).readEntity(String.class);
    }
}
