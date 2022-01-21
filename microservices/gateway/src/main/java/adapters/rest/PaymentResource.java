package adapters.rest;

import event.payment.history.PaymentHistoryReplied;
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
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(reply.getFailureResponse())
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/customer")
    public Response customerPaymentHistory(@QueryParam("customerId") UUID customerId) {
        System.out.println("Customer mayment history on " + Thread.currentThread().getName());
        PaymentHistoryReplied.PaymentCustomerHistoryReplied reply = paymentService.customerPaymentHistory(customerId);
        if (reply.isSuccess()) {
            return Response.status(Response.Status.OK)
                    .entity(reply.getSuccessResponse().getCustomerHistoryList())
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(reply.getFailureResponse().getReason())
                    .build();
        }
    }

    @GET
    @Path("/merchant")
    @Produces(MediaType.APPLICATION_JSON)
    public Response merchantPaymentHistory(@QueryParam("merchantId") UUID merchantId) {
        System.out.println("Merchant payment history on " + Thread.currentThread().getName());
        PaymentHistoryReplied.PaymentMerchantHistoryReplied reply = paymentService.merchantPaymentHistory(merchantId);
        if (reply.isSuccess()) {
            return Response.status(Response.Status.OK)
                    .entity(reply.getSuccessResponse().getMerchantHistoryList())
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(reply.getFailureResponse().getReason())
                    .build();
        }
    }

    @GET
    @Path("/manager")
    @Produces(MediaType.APPLICATION_JSON)
    public Response managerPaymentManagerHistory() {
        System.out.println("Manager payment history on " + Thread.currentThread().getName());
        PaymentHistoryReplied.PaymentManagerHistoryReplied reply = paymentService.managerPaymentHistory();
        if (reply.isSuccess()) {
            return Response.status(Response.Status.OK)
                    .entity(reply.getSuccessResponse().getManagerHistoryList())
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(reply.getFailureResponse().getReason())
                    .build();
        }
    }

}