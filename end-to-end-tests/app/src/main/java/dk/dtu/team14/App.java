package dk.dtu.team14;

import rest.RegisterUser;
import rest.RetireUser;
import rest.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

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


    public String registerUser(String bankAccountId, String cprNumber, String name, Boolean isMerchant) {
        return webTarget.path("accounts").request().post(Entity.json(new RegisterUser(
                bankAccountId,
                name,
                cprNumber,
                isMerchant
        ))).readEntity(String.class);
    }

    public String retireUser(String customerId) {
        return webTarget.request().post(Entity.json(new RetireUser(
                customerId
        ))).readEntity(String.class);
    }
}
