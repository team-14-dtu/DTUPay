package adapters.rest;

import event.token.TokensReplied;
import rest.TokensRequested;
import services.TokenService;
import services.errors.DTUPayError;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tokens")
public class TokenResource {

    private final TokenService service;

    @Inject
    public TokenResource(TokenService service) {
        this.service = service;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestTokens(TokensRequested requestTokens) {
        TokensReplied reply = service.requestTokens(requestTokens.customerId, requestTokens.numberOfTokens);

        System.out.println("REAL REPLY: "+reply);

        if (reply.isSuccess()) {
            return Response.status(Response.Status.OK)
                    .entity(reply.getSuccessResponse())
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(reply.getFailureResponse())
                    .build();
        }
    }
}