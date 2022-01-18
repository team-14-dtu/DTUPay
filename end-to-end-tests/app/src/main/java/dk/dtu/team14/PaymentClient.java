package dk.dtu.team14;

import rest.PaymentRequest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.UUID;

public class PaymentClient extends Client {

    public Response pay(UUID tokenId, UUID merchantId, BigDecimal amount, String description) { //TODO amount should be an integer
        final PaymentRequest request = new PaymentRequest(
                merchantId, amount, tokenId, description
        );

        return webTarget.path("payments")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(request));
    }
//
//    public List<Payment> paymentHistory(String userId, User.Type userType) {
//        return app.webTarget.path("payments")
//                .queryParam("user", userId).queryParam("type", userType)
//                .request()
//                .accept(MediaType.APPLICATION_JSON)
//                .get(Response.class)
//                .readEntity(new GenericType<>() {
//                });
//    }

}
