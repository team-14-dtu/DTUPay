package client;

import dk.dtu.team14.CustomerClient;
import event.token.TokensReplied;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
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

    User customer = new User();
    List<UUID> tokens = new ArrayList<>();

    @Before
    public void createNewCustomer() {
        customer.setUserId(UUID.randomUUID());
    }

    @Given("a customer with {int} tokens")
    public void aCustomerWithTokens(int numberOfTokens) {
        //Simulate the number of tokens that the customer already has
        for (int i=0; i<numberOfTokens; i++ ) {
            UUID token = UUID.randomUUID();
            tokens.add(token);
        }
        customer.setTokens(tokens);
        assertEquals(numberOfTokens,customer.getTokens().size());
    }

    /*@Given("a customer with id {string}")
    public void aCustomerWithId(String cid) {
        UUID uuidCid = UUID.nameUUIDFromBytes(cid.getBytes());
        customer.setUserType(User.Type.CUSTOMER);
        customer.setUserName("Naja Jean Larsen");
        customer.setCpr("010101-0808");
        customer.setAccountId("9876543");
        customer.setUserId(uuidCid);
    }

    @Given("the customer has {int} tokens")
    public void theCustomerHasTokens(Integer numberOfTokens) {
        //Simulate the number of tokens that the customer already has
        for (int i=0; i<numberOfTokens; i++ ) {
            UUID token = UUID.randomUUID();
            tokens.add(token);
        }
        customer.setTokens(tokens);
        assertEquals(numberOfTokens.longValue(),customer.getTokens().size());
    }*/

    @When("a customer requests {int} tokens")
    public void aCustomerRequestsTokens(Integer numberOfTokens) {
        response = customerService.requestTokens(customer.getUserId(), numberOfTokens);

        System.out.println("WOHOO"+response.getStatus());

        List<UUID> newTokens;
        if (response.getStatus() == 200) {
            System.out.println("I got here!");

            newTokens = response.readEntity(TokensReplied.Success.class).getTokens();
            System.out.println(newTokens);
        } else {
            newTokens = response.readEntity(TokensReplied.Failure.class).getTokens();
        }

        tokens.addAll(newTokens);
        customer.setTokens(tokens);
    }

    @Then("the customer now has {int} tokens")
    public void theCustomerNowHasTokens(Integer numberOfTokens) {
        assertEquals(numberOfTokens.longValue(),customer.getTokens().size());
    }

}
