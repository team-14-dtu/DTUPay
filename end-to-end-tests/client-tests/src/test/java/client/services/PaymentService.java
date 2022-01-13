package client.services;

import rest.Payment;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class PaymentService {

    Client client;
    WebTarget webTarget;


    public PaymentService(String baseUrl) {
        this.client = ClientBuilder.newClient();
        this.webTarget = client.target(baseUrl);
    }

    public List<Payment> getPaymentsForUser(String userId) {
        return webTarget.path("payments").path("user=" + userId)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Response.class)
                .readEntity(new GenericType<List<Payment>>() {});
    }

    public List<Payment> getAllPayments() {
        return webTarget.path("payments")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Response.class)
                .readEntity(new GenericType<List<Payment>>() {});
    }

    public Payment getTargetPayment(String paymentId) {
        return webTarget.path("payments").path(paymentId)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Response.class)
                .readEntity(new GenericType<Payment>() {});
    }

    public Response pay(String tokenId, String merchantId, double amount, String description) { //TODO amount should be an integer
        Payment payment = new Payment(tokenId, merchantId, "customerId1", amount, description);
        return webTarget.path("payments").path("pay")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(payment));
    }
}
