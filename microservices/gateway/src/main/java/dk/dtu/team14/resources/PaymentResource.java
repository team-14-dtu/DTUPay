package dk.dtu.team14.resources;

import dk.dtu.team14.services.PaymentService;
import messaging.Event;
import rest.Payment;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static event.PaymentEvents.*;

@Path("/payments")
public class PaymentResource {

    private final PaymentService paymentService;

    @Inject
    public PaymentResource(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{paymentId},{customerId},{merchantId},{amount},{description}")
    public Response createPayment(@PathParam("paymentId") String paymentId, @PathParam("customerId") String customerId, @PathParam("merchantId") String merchantId, @PathParam("amount") String amount, @PathParam("description") String description) {
        String payment = paymentId + "," + customerId + "," + merchantId + "," + amount + "," + description;
        Event event = new Event(getPaymentRequestTopics(), new Object[]{payment});
        paymentService.publishEvent(event);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user/{userId}")
    public List<Payment> getPaymentForUser(@PathParam("userId") String userId) {
        Event event = new Event(getHistoryRequestTopics(), new Object[]{userId});
        paymentService.publishEvent(event);
        var response = paymentService.getPaymentsForUser.join();
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{paymentId}")
    public List<Payment> getTargetPayment(@PathParam("paymentId") String paymentId) {
        Event event = new Event(getTargetPaymentRequestTopics(), new Object[]{paymentId});
        paymentService.publishEvent(event);
        var response = paymentService.getTargetPayment.join();
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Payment> getAllPayments() {
        Event event = new Event(getAllHistoryRequestTopics(), new Object[]{});
        paymentService.publishEvent(event);
        var response = paymentService.getAllPayments.join();
        return response;
    }
}