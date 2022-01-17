package adapters.rest;

import rest.User;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class Resource {

    @GET
    @Path("/")
    public String greeting() {
        return "Team 14 rulez";
    }
}