package dk.dtu.team14;

import com.fasterxml.jackson.core.type.TypeReference;
import event.token.ReplyTokens;
import event.token.RequestTokens;
import com.fasterxml.jackson.databind.ObjectMapper;
import rest.RegisterUser;
import rest.Token;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//import static io.cucumber.messages.JSON.mapper;

public class CustomerService {

	WebTarget baseUrl;

	public CustomerService() {
		Client client = ClientBuilder.newClient();
		baseUrl = client.target("http://localhost:8080/");
	}

	public List<Token> requestTokens(String customerID, int numberOfTokens) {

		RequestTokens requestTokens = new RequestTokens(customerID, numberOfTokens);

		Response entity = baseUrl.path("tokens").request().post(Entity.json(requestTokens));
		ReplyTokens replyTokens = entity.readEntity(ReplyTokens.class);

		List<Token> dr = Arrays.stream(objects)
				.map(object -> mapper.convertValue(object, Token.class))
				.collect(Collectors.toList());

		List<Token> response= dr;

		System.out.println("Response: " + response);

		return replyTokens.getTokens();
	}

}
