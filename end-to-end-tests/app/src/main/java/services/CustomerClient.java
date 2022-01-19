package services;

import rest.TokensRequested;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.UUID;

public class CustomerClient extends Client {

    public Response requestTokens(UUID customerID, int numberOfTokens) {
        TokensRequested requestTokens = new TokensRequested(customerID, numberOfTokens);
        return webTarget
                .path("tokens")
                .request().post(Entity.json(requestTokens));
    }

}
