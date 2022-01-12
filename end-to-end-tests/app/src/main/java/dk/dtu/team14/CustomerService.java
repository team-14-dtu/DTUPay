package paymentService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dtu_team14.model.Payment;

import java.math.BigDecimal;
import java.util.List;

enum Type {
	CUSTOMER,
	MERCHANT
}

public class PaymentService {

	WebTarget baseUrl;

	public PaymentService() {
		Client client = ClientBuilder.newClient();
		baseUrl = client.target("http://localhost:8080/");
	}

	public List<Payment> getPayments() {
		return baseUrl.path("payments")
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get(Response.class)
				.readEntity(new GenericType<List<Payment>>() {});
	}

	public Response pay(Payment payment) {
		return baseUrl.path("payments")
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(payment));
	}
	public Response payTo(Payment payment) {
		return baseUrl.path("payments/to")
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(payment));
	}

	public Response registerUser(String firstname, String lastname, String cprNumber, String accountId, Type type) {
		return baseUrl.path("users")
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(new UserRequest(firstname, lastname, cprNumber, accountId, type)));
	}


}
