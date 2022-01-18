package adapters.rest;

import event.token.TokensReplied;
import rest.TokensRequested;
import services.TokenService;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/tokens")
public class TokenResource {

    private final TokenService service;

    @Inject
    public TokenResource(TokenService service) {
        this.service = service;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public TokensReplied requestTokens(TokensRequested requestTokens) {
        return service.requestTokens(requestTokens.customerId, requestTokens.numberOfTokens);
    }
}