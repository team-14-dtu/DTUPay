package adapters.rest;

import event.payment.history.ReplyPaymentHistory;
import rest.PaymentHistory;
import rest.User;
import services.PaymentService;
import messaging.Event;
import rest.Payment;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static event.payment.PaymentEvents.*;

@Path("/payments")
public class PaymentResource {

    private final PaymentService paymentService;

    @Inject
    public PaymentResource(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String pay(Payment payment) {
        System.out.println("Pay on " + Thread.currentThread().getName());
        return paymentService.pay(payment);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReplyPaymentHistory> paymentHistory(@QueryParam("user") String userId, @QueryParam("type") User.Type type) {
        PaymentHistory user = new PaymentHistory(userId, type);
        System.out.println("Payment history on " + Thread.currentThread().getName());
        return paymentService.paymentHistory(user);
    }

}