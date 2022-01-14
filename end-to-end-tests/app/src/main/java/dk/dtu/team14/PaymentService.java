package dk.dtu.team14;

import rest.Payment;
import rest.User;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

public class PaymentService {

    App app;

    public PaymentService(String baseUrl) {
        app = new App(baseUrl);
    }

    public List<Payment> getPaymentsForUser(String userId, User.Type userType) {
        return app.webTarget.path("payments").path("history").queryParam("user", userId).
                queryParam("type", userType)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Response.class)
                .readEntity(new GenericType<List<Payment>>() {});
    }

    public List<Payment> getAllPayments() {
        return app.webTarget.path("payments")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Response.class)
                .readEntity(new GenericType<List<Payment>>() {});
    }

    public Payment getTargetPayment(String paymentId) {
        return app.webTarget.path("payments").path(paymentId)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Response.class)
                .readEntity(new GenericType<Payment>() {});
    }

    public Response pay(String tokenId, String customerId, String merchantId, BigDecimal amount, String description) { //TODO amount should be an integer
        Payment payment = new Payment(customerId, merchantId, "customerId1", amount, description);
        return app.webTarget.path("payments").path("pay")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(payment));
    }


}
