package dk.dtu.team14;

import event.token.RequestTokens;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CustomerService {

	WebTarget baseUrl;

	public CustomerService() {
		Client client = ClientBuilder.newClient();
		baseUrl = client.target("http://localhost:8080/");
	}

	public Response requestTokens(String customerID, int numberOfTokens) {
		RequestTokens requestTokens = new RequestTokens(customerID, numberOfTokens);
		return baseUrl.path("tokens")
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(requestTokens));
	}

}
