package client.paymentservice;

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

    public List<Payment> getPayments(String id) {
        return webTarget.path("payments").path(id)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Response.class)
                .readEntity(new GenericType<List<Payment>>() {});
    }
}
