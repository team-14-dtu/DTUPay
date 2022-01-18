package dk.dtu.team14;

import event.token.TokensReplied;
import rest.TokensRequested;
import rest.Token;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//import static io.cucumber.messages.JSON.mapper;

public class CustomerService {

	WebTarget baseUrl;

	public CustomerService() {
		Client client = ClientBuilder.newClient();
		baseUrl = client.target("http://localhost:8080/");
	}

	public List<UUID> requestTokens(UUID customerID, int numberOfTokens) {

		TokensRequested requestTokens = new TokensRequested(customerID, numberOfTokens);
		Response entity = baseUrl.path("tokens").request().post(Entity.json(requestTokens));
		TokensReplied reply = entity.readEntity(TokensReplied.class);

		List<UUID> tokens = reply.getSuccessResponse().getTokens(); //TODO: handle when this is not a success

		return tokens;
	}

}
