package services.Customer;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

abstract public class CustomerClient {
    protected WebTarget webTarget;

    public CustomerClient() {
        final var client = ClientBuilder.newClient();

        String baseUrl = "http://localhost:8080";
        this.webTarget = client.target(baseUrl);
    }
}
