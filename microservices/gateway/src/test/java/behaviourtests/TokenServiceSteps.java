package behaviourtests;

import event.token.ReplyTokens;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import services.TokenService;
import messaging.MessageQueue;
import rest.Token;
import rest.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TokenServiceSteps {
    private MessageQueue q = mock(MessageQueue.class);
    private TokenService service = new TokenService(q);
    private CompletableFuture<List<Token>> listOfTokens = new CompletableFuture<>();

    User customer = new User();
    List<Token> tokens = new ArrayList<>();
    int tokenAmountRequested;

    @Given("a customer with customerId {string}")
    public void a_customer_with_customer_id(String cid) {
        UUID uuidCid = UUID.nameUUIDFromBytes(cid.getBytes());

        customer.setUserType(User.Type.CUSTOMER);
        customer.setUserId(uuidCid);
    }
    @Given("the customer has {int} token")
    public void the_customer_has_token(Integer numberOfTokens) {
        for (int i=0; i<numberOfTokens; i++ ) {
            Token t = new Token(customer.getUserId());
            t.tokenId = UUID.nameUUIDFromBytes(("testToken").getBytes());
            tokens.add(t);
        }
        customer.setTokens(tokens);
    }
    @When("the customer request {int} tokens")
    public void the_customer_request_tokens(Integer numberOfTokens) {
        this.tokenAmountRequested = numberOfTokens;
        new Thread(() -> {
            ReplyTokens result = service.requestTokens(customer.getUserId(),numberOfTokens);

            customer.setTokens(result.getTokens());
            listOfTokens.complete(result.getTokens());
        }).start();
    }
    @Then("the {string} event is sent")
    public void the_event_is_sent(String topic) {
        Event event = new Event(topic, new Object[] { customer.getUserId(), tokenAmountRequested });
        verify(q).publish(event);
    }
    @When("the {string} event is received with a list of {int} tokens")
    public void the_event_is_received_with_a_list_of_tokens(String topic, int newTokenAmount) {
        // This step simulates the event created by token service.
        assertEquals(topic,ReplyTokens.getEventName());

        if (customer.getTokens().size() <= 1) {
            for (int i=0; i<tokenAmountRequested; i++ ) {
                Token t = new Token(customer.getUserId());
                t.setTokenId(UUID.nameUUIDFromBytes(("generatedTestToken").getBytes()));
                tokens.add(t);
            }
        }

        service.tokenReceived(new Event(topic, new Object[] {tokens}));
    }
    @Then("the customer now has {int} tokens")
    public void the_customer_now_has_tokens(Integer numberOfTokens) {
        listOfTokens.join();

        int actualNumberOfTokens = customer.getTokens().size();
        assertEquals(numberOfTokens.longValue(), actualNumberOfTokens);
    }
}
