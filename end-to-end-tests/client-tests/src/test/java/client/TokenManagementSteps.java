package client;

import dk.dtu.team14.CustomerClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertEquals;

import rest.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TokenManagementSteps {

    CustomerClient customerService = new CustomerClient();

    User customer = new User();
    List<UUID> tokens = new ArrayList<>();

    @Given("a customer with id {string}")
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
    }

    @When("a customer requests {int} tokens")
    public void aCustomerRequestsTokens(Integer numberOfTokens) {
        System.out.println("Requesting "+numberOfTokens+" tokens from "+customer.getUserId()+" with tokensamount: "+customer.getTokens().size());
        List<UUID> newTokens = customerService.requestTokens(customer.getUserId(), numberOfTokens);
        tokens.addAll(newTokens);
        customer.setTokens(tokens);
    }

    @Then("the customer now has {int} tokens")
    public void theCustomerNowHasTokens(Integer numberOfTokens) {
        assertEquals(numberOfTokens.longValue(),customer.getTokens().size());
    }

}
