package adapters.rest;

import event.token.ReplyTokens;
import event.token.RequestTokens;
import rest.Token;
import rest.User;
import services.TokenService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/tokens")
public class TokenResource {

    private final TokenService service;

    @Inject
    public TokenResource(TokenService service) {
        this.service = service;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public ReplyTokens requestTokens(RequestTokens requestTokens) {
        System.out.println("We hit the gateway!!!");
        System.out.println(requestTokens.customerId);
        System.out.println(requestTokens.numberOfTokens);
        return service.requestTokens(requestTokens.customerId, requestTokens.numberOfTokens);
    }
}