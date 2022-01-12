package adapters.rest;

import event.token.RequestTokens;
import rest.Token;
import rest.User;
import services.Service;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/tokens")
public class TokenResource {

    private final Service service;

    @Inject
    public TokenResource(Service service) {
        this.service = service;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public List<Token> requestTokens(RequestTokens requestTokens) {
        return service.requestTokens(requestTokens.getCustomerId(), requestTokens.getNumberOfTokens());
    }
}