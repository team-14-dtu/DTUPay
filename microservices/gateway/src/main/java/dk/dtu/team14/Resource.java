package dk.dtu.team14;

import messaging.Event;
import rest.Payment;
import rest.User;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
public class Resource {

    private final Service service;

    private static final String PAYMENT_TOPIC = "PAYMENT";
    private static final String HISTORY_TOPIC = "HISTORY_REQUEST";

    @Inject
    public Resource(Service service) {
        this.service = service;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public User greeting() {
        return service.hello();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/payments/{userId}")
    public List<Payment> getPaymentForUser(@PathParam("userId") String userId) {
        System.out.println("received REST message");
        Event event = new Event(PAYMENT_TOPIC + "." + HISTORY_TOPIC, new Object[]{userId});
        service.publishEvent(event);

        var response = service.paymentGot.join();

        return response;
    }

    @POST
    @Path("/payments/{userId}")
    public Response putPaymentForUser(User user) {
        try {
            //TODO: below is incorrect, fix it
            Event event = new Event(PAYMENT_TOPIC + "." + HISTORY_TOPIC, new User[]{user});
            service.publishEvent(event);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }

    }
}