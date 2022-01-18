package adapters.rest;


import rest.RegisterUser;
import rest.RetireUser;
import services.AccountService;
import services.errors.DTUPayError;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("delete")
    public Response retireUser(RetireUser retireUserRequest) {
        try {
            accountService.retireUser(retireUserRequest);
            return Response.ok().build();
        } catch (DTUPayError e) {
            return e.convertToResponse();
        }
    }
}
