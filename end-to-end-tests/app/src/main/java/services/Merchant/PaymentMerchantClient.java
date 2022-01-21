package services.Merchant;

import rest.PaymentRequested;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.UUID;

public class PaymentMerchantClient extends MerchantClient {

    public Response pay(UUID tokenId, UUID merchantId, BigDecimal amount, String description) {
        final PaymentRequested request = new PaymentRequested(
                merchantId, amount, tokenId, description
        );

        return webTarget.path("payments")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(request));
    }

    public Response merchantPaymentHistory(UUID merchantId) {
        return webTarget.path("payments").path("merchant")
                .queryParam("merchantId", merchantId)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Response.class);
    }

}
