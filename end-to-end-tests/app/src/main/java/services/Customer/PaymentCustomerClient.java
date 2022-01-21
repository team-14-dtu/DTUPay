package services.Customer;

// @author : Søren

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

public class PaymentCustomerClient extends CustomerClient {

    public Response customerPaymentHistory(UUID customerId) {
        return webTarget.path("payments").path("customer")
                .queryParam("customerId", customerId)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Response.class);
    }

}
