package adapters.rest;

import event.payment.history.PaymentHistoryReplied;
import rest.PaymentHistory;
import rest.PaymentRequest;
import rest.User;
import services.PaymentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/payments")
public class PaymentResource {

    private final PaymentService paymentService;

    @Inject
    public PaymentResource(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String pay(PaymentRequest payment) {
        return paymentService.pay(payment);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PaymentHistoryReplied> paymentHistory(@QueryParam("user") String userId, @QueryParam("type") User.Type type) {
        PaymentHistory user = new PaymentHistory(userId, type);
        System.out.println("Payment history on " + Thread.currentThread().getName());
        return paymentService.paymentHistory(user);
    }

}