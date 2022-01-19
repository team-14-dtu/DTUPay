package adapters.rest;

import rest.*;
import services.PaymentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/payments")
public class PaymentResource {

    private final PaymentService paymentService;

    @Inject
    public PaymentResource(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response pay(PaymentRequested payment) {
        var reply = paymentService.pay(payment);
        if (reply.isSuccess()) {
            return Response.status(Response.Status.OK)
                    .entity(reply.getSuccessResponse())
                    .build();
        } else {
            // TODO: Throw, so that the status code is 400
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(reply.getFailureResponse())
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/customer")
    public List<PaymentHistoryCustomer> customerPaymentHistory(@QueryParam("customerId") UUID customerId) {
        System.out.println("Customer mayment history on " + Thread.currentThread().getName());
        return paymentService.customerPaymentHistory(customerId);
    }

    @GET
    @Path("/merchant")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PaymentHistoryMerchant> merchantPaymentHistory(@QueryParam("merchantId") UUID merchantId) {
        System.out.println("Merchant payment history on " + Thread.currentThread().getName());
        return paymentService.merchantPaymentHistory(merchantId);
    }

    @GET
    @Path("/manager")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PaymentHistoryManager> managerPaymentManagerHistory() {
        System.out.println("Manager payment history on " + Thread.currentThread().getName());
        List<PaymentHistoryManager> check = paymentService.managerPaymentHistory();
        return check;
    }

}