package services.Manager;

import rest.PaymentRequested;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.UUID;

public class PaymentManagerClient extends ManagerClient {

    public Response pay(UUID tokenId, UUID merchantId, BigDecimal amount, String description) {
        final PaymentRequested request = new PaymentRequested(
                merchantId, amount, tokenId, description
        );

        return webTarget.path("payments")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(request));
    }

    public Response customerPaymentHistory(UUID customerId) {
        return webTarget.path("payments").path("customer")
                .queryParam("customerId", customerId)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Response.class);
    }

    public Response merchantPaymentHistory(UUID merchantId) {
        return webTarget.path("payments").path("merchant")
                .queryParam("merchantId", merchantId)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Response.class);
    }

    public Response managerPaymentHistory() {
        return webTarget.path("payments").path("manager")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Response.class);
    }

}
