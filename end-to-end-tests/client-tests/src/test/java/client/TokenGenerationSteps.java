package client;

import dk.dtu.team14.CustomerClient;
import event.token.TokensReplied;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertEquals;

import rest.User;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TokenGenerationSteps {

    CustomerClient customerService = new CustomerClient();

    Response response;
    String errorMessageRecieved;

    User customer = new User();

    @Given("a customer with no tokens")
    public void aCustomerNoTokens() {
        //Simulate the number of tokens that the customer already has
        customer.setTokens(new ArrayList<>());
        assertEquals(0,customer.getTokens().size());
    }

    @When("the customer requests {int} tokens")
    public void theCustomerRequestsTokens(Integer numberOfTokens) {
        response = customerService.requestTokens(customer.getUserId(), numberOfTokens);

        List<UUID> newTokens;
        if (response.getStatus() == 200) {
            newTokens = response.readEntity(TokensReplied.Success.class).getTokens();
            assertEquals(200,response.getStatus());
        } else {
            TokensReplied.Failure failResponse = response.readEntity(TokensReplied.Failure.class);
            newTokens = failResponse.getTokens();
            errorMessageRecieved = failResponse.getReason();
            assertEquals(400,response.getStatus());
        }

        customer.setTokens(newTokens);
    }

    @Then("the customer now has {int} tokens")
    public void theCustomerNowHasTokens(Integer numberOfTokens) {
        assertEquals(numberOfTokens.longValue(),customer.getTokens().size());
    }

    @Then("an error message is returned saying {string}")
    public void anErrorMessageIsReturnedSaying(String errorMessage) {
        assertEquals(errorMessage, errorMessageRecieved);
    }
}
