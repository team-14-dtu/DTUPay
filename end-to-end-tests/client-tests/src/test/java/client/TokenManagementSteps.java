package client;

import dk.dtu.team14.CustomerService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertEquals;
import rest.Token;
import rest.User;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class TokenManagementSteps {

    CustomerService customerService = new CustomerService();

    User customer = new User();
    List<Token> tokens = new ArrayList<>();

    @Given("a customer with id {string}")
    public void aCustomerWithId(String id) {
        customer.setUserType(User.Type.CUSTOMER);
        customer.setUserName("Naja Jean Larsen");
        customer.setCpr("010101-0808");
        customer.setAccountId("9876543");
        customer.setUserId(id);
    }

    @Given("the customer has {int} tokens")
    public void theCustomerHasTokens(Integer numberOfTokens) {
        //Simulate the number of tokens that the customer already has
        for (int i=0; i>numberOfTokens; i++ ) {
            Token token = new Token(customer.getUserId());
            tokens.add(token);
        }
        customer.setTokens(tokens);
    }

    @When("a customer requests {int} tokens")
    public void aCustomerRequestsTokens(Integer numberOfTokens) {
        //List<Token> newTokens = customerService.requestTokens(customer.getUserId(), numberOfTokens);
        List<Token> newTokens = customerService.requestTokens(customer.getUserId(), numberOfTokens);
        System.out.println(newTokens);
        //List<Token> previousTokens = customer.getTokens();
        //customer.setTokens(previousTokens.addAll(newTokens));
    }

    @Then("the customer now has {int} tokens")
    public void theCustomerNowHasTokens(Integer numberOfTokens) {
        //assertEquals(customer.getTokens(), numberOfTokens);
    }

}
