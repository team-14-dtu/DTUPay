package adapters.rest;
// @author : Petr
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class Resource {

    @GET
    @Path("/")
    public String greeting() {
        return "Team 14 rulez";
    }
}