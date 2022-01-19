package services;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

abstract public class Client {
    protected WebTarget webTarget;

    public Client() {
        final var client = ClientBuilder.newClient();

        String baseUrl = "http://localhost:8080";
        this.webTarget = client.target(baseUrl);
    }
}
