package dk.dtu.team14;

import com.fasterxml.jackson.core.type.TypeReference;
import event.token.RequestTokens;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.ObjectMapper;
import rest.RegisterUser;
import rest.Token;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

public class CustomerService {

	WebTarget baseUrl;

	public CustomerService() {
		Client client = ClientBuilder.newClient();
		baseUrl = client.target("http://localhost:8080/");
	}

	public List<Token> requestTokens(String customerID, int numberOfTokens) {

		RequestTokens requestTokens = new RequestTokens(customerID, numberOfTokens);
		Token[] T = baseUrl.path("tokens").request().post(Entity.json(requestTokens)).readEntity(Token[].class);
		List<Token> response = Arrays.asList(T);


		return response;
	}

}
