package services.Manager;

// @author : Petr

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

abstract public class ManagerClient {
    protected WebTarget webTarget;

    public ManagerClient() {
        final var client = ClientBuilder.newClient();

        String baseUrl = "http://localhost:8080";
        this.webTarget = client.target(baseUrl);
    }
}
