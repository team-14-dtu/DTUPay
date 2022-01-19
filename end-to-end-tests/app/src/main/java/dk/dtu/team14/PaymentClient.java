package dk.dtu.team14;

import rest.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class PaymentClient extends Client {

    public Response pay(UUID tokenId, UUID merchantId, BigDecimal amount, String description) {
        final PaymentRequest request = new PaymentRequest(
                merchantId, amount, tokenId, description
        );

        return webTarget.path("payments")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(request));
    }

    public List<PaymentHistoryCustomer> customerPaymentHistory(UUID customerId) {
        return webTarget.path("payments").path("customer")
                .queryParam("customerId", customerId)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Response.class)
                .readEntity(new GenericType<>() {
                });
    }

    public List<PaymentHistoryMerchant> merchantPaymentHistory(UUID merchantId) {
        return webTarget.path("payments").path("merchant")
                .queryParam("merchantId", merchantId)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Response.class)
                .readEntity(new GenericType<>() {
                });
    }

    public List<PaymentHistoryManager> managerPaymentHistory() {
        return webTarget.path("payments").path("manager")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Response.class)
                .readEntity(new GenericType<>() {
                });
    }

}
