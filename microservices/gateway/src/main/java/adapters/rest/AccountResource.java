package adapters.rest;


import rest.RegisterUser;
import services.AccountService;
import services.errors.DTUPayError;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/accounts")
public class AccountResource {

    private final AccountService accountService;

    public AccountResource(AccountService service) {
        this.accountService = service;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(RegisterUser user) {
        try {
            var userId = accountService.registerUser(user);
            return Response.ok(userId).build();
        } catch (DTUPayError e) {
            return e.convertToResponse();
        }
    }

    @DELETE
    @Path("{cpr}")
    public Response retireUser(@PathParam("cpr") String cpr) {
        try {
            accountService.retireUser(cpr);
            return Response.ok().build();
        } catch (DTUPayError e) {
            return e.convertToResponse();
        }
    }
}
