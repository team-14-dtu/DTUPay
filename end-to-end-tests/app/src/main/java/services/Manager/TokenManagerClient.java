package services.Manager;

// @author : Mathilde

import rest.TokensRequested;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.UUID;

public class TokenManagerClient extends ManagerClient {

    public Response requestTokens(UUID customerID, int numberOfTokens) {
        TokensRequested requestTokens = new TokensRequested(customerID, numberOfTokens);
        return webTarget
                .path("tokens")
                .request().post(Entity.json(requestTokens));
    }

}
