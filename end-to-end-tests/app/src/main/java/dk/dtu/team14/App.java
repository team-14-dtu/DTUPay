package dk.dtu.team14;

import rest.RegisterUser;
import rest.RetireUser;
import rest.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.util.List;
import java.util.UUID;

public class App {
    Client client;
    WebTarget webTarget;

    private String baseUrl = "http://localhost:8080";

    public App() {
        this.client = ClientBuilder.newClient();
        this.webTarget = client.target(baseUrl);
    }

    public User getResponse() {
        return webTarget.request().get(User.class);
    }


    public UUID registerUser(String bankAccountId, String cprNumber, String name, Boolean isMerchant) {
        return webTarget.path("accounts").request().post(Entity.json(new RegisterUser(
                bankAccountId,
                name,
                cprNumber,
                isMerchant
        ))).readEntity(UUID.class);
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
