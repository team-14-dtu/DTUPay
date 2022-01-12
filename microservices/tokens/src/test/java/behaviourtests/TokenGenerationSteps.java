package behaviourtests;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import services.TokenManagementService;
import messaging.MessageQueue;
import rest.Token;
import rest.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.mock;

public class TokenGenerationSteps {
    private MessageQueue q = mock(MessageQueue.class);
    private TokenManagementService service = new TokenManagementService(q);
    private CompletableFuture<List<Token>> listOfTokens = new CompletableFuture<>();

    User customer = new User();

    @Given("a customer with customerId {string}")
    public void a_customer_with_customer_id(String id) {
        customer.setUserType(User.Type.CUSTOMER);
        customer.setUserName("Naja Jean Larsen");
        customer.setCpr("010101-0808");
        customer.setAccountId("9876543");
        customer.setUserId(id);
    }
    @Given("the customer has {int} token")
    public void the_customer_has_token(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @When("the customer request {int} tokens")
    public void the_customer_request_tokens(Integer int1) {
        new Thread(() -> {
            var result = service.
            listOfTokens.complete(result);
        }).start();

        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("the {string} event is sent")
    public void the_event_is_sent(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @When("the {string} event is sent with a list of tokens")
    public void the_event_is_sent_with_a_list_of_tokens(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("the customer now has {int} tokens")
    public void the_customer_now_has_tokens(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

}
