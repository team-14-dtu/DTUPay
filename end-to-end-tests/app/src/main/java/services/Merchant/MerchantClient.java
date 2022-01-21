package services.Merchant;

// @author : Petr

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

abstract public class MerchantClient {
    protected WebTarget webTarget;

    public MerchantClient() {
        final var client = ClientBuilder.newClient();

        String baseUrl = "http://localhost:8080";
        this.webTarget = client.target(baseUrl);
    }
}
