package dk.dtu.team14;

import event.token.RequestTokens;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.ObjectMapper;
import rest.Token;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerService {

	WebTarget baseUrl;

	public CustomerService() {
		Client client = ClientBuilder.newClient();
		baseUrl = client.target("http://localhost:8080/");
	}

	public List<Token> requestTokens(String customerID, int numberOfTokens) {

		RequestTokens requestTokens = new RequestTokens(customerID, numberOfTokens);
		Object[] objects = baseUrl.path("tokens").request().post(Entity.json(requestTokens)).readEntity(Object[].class);
		System.out.println("I'm here!!!!");
		ObjectMapper mapper = new ObjectMapper();

		List<Token> dr = Arrays.stream(objects)
				.map(object -> mapper.convertValue(object, Token.class))
				.collect(Collectors.toList());

		List<Token> response= dr;

		System.out.println("Response: " + response);

		return response;
	}

}
