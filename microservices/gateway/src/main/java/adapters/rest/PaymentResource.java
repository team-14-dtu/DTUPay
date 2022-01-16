package adapters.rest;

import rest.User;
import services.PaymentService;
import messaging.Event;
import rest.Payment;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
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
    @Path("/pay")
    public Response createPayment(Payment payment) { //TODO receive token
        Event event = new Event(getPaymentRequestTopics(), new Object[]{payment});
        synchronized (paymentService.createPayment)
        {
            paymentService.publishEvent(event);
            paymentService.createPayment.join();
            paymentService.createPayment = new CompletableFuture<>();
        }
        return Response.ok().build(); //TODO treat and send error message when appropriate
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/history")
    public List<Payment> getPaymentForUser(@QueryParam("user") String userId, @QueryParam("type") User.Type type) {
        Event event = new Event(getHistoryRequestTopics(), new Object[]{userId, type});
        List<Payment> response;
        synchronized (paymentService.getPaymentsForUser)
        {
            paymentService.publishEvent(event);
            response = paymentService.getPaymentsForUser.join();
            paymentService.getPaymentsForUser = new CompletableFuture<>();
        }
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/paymentId")
    public Payment getTargetPayment(@QueryParam("paymentId") UUID paymentId) {
        Event event = new Event(getTargetPaymentRequestTopics(), new Object[]{paymentId});
        Payment response;
        synchronized (paymentService.getTargetPayment)
        {
            paymentService.publishEvent(event);
            response = paymentService.getTargetPayment.join();
            paymentService.getTargetPayment = new CompletableFuture<>();
        }
        return response;
    }


}