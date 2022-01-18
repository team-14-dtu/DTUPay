package dk.dtu.team14;

import event.token.TokensReplied;
import rest.TokensRequested;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

//import static io.cucumber.messages.JSON.mapper;

public class CustomerClient extends dk.dtu.team14.Client {

    public List<UUID> requestTokens(UUID customerID, int numberOfTokens) {

        TokensRequested requestTokens = new TokensRequested(customerID, numberOfTokens);
        Response entity = webTarget.path("tokens").request().post(Entity.json(requestTokens));
        TokensReplied reply = entity.readEntity(TokensReplied.class);

        List<UUID> tokens = reply.getTokens();

        return tokens;
    }

}
