package services.errors;
// @author : Petr
import javax.ws.rs.core.Response;

public class DTUPayError extends Exception {
    public final String reason;

    public DTUPayError(String reason) {
        this.reason = reason;
    }

    public Response convertToResponse() {
        return Response.status(400).entity(reason).build();
    }
}
