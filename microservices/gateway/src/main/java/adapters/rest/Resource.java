package adapters.rest;

import rest.User;
import services.Service;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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