import messaging.Event;
import rest.Payment;
import rest.User;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
public class Resource {

    private final Service service;

    private static final String PAYMENT_TOPIC = "PAYMENT_TOPIC";
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
    public List<Payment> getPaymentForUser(User user) {

        Event event = new Event(PAYMENT_TOPIC + "." + HISTORY_TOPIC, new User[]{user});
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