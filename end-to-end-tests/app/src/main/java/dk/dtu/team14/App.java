package dk.dtu.team14;

import rest.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class App {
    Client client;
    WebTarget webTarget;


    public App(String baseUrl) {
        this.client = ClientBuilder.newClient();
        this.webTarget = client.target(baseUrl);
    }

    public User getResponse() {
        return webTarget.request().get(User.class);
    }

}
