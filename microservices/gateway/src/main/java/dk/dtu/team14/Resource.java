package dk.dtu.team14;

import rest.User;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
public class Resource {

    private final Service service;

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
}