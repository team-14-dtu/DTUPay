package adapters.rest;


import rest.RetireUser;
import rest.RegisterUser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/accounts")
public class AccountResource {



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String registerUser(RegisterUser user) {
        String costumerId = "";
        return costumerId;
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public String retireUser(RetireUser user) {

        return "";
    }
}
