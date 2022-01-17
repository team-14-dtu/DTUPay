package adapters.rest;


import rest.RetireUser;
import rest.RegisterUser;
import services.AccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/accounts")
public class AccountResource {

    private final AccountService accountService;

    public AccountResource(AccountService service) {
        this.accountService = service;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String registerUser(RegisterUser user) {
        return accountService.registerUser(user);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("delete")
    public String retireUser(RetireUser retireUserRequest) {
        return accountService.retireUser(retireUserRequest);
    }
}
