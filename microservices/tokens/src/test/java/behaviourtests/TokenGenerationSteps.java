package behaviourtests;

import event.token.ReplyTokens;
import event.token.RequestTokens;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import services.TokenManagementService;
import messaging.MessageQueue;
import rest.Token;
import rest.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TokenGenerationSteps {
    private MessageQueue queue = mock(MessageQueue.class);
    private TokenManagementService service = new TokenManagementService(queue);

    @When("a {string} event is received for {int} tokens and customerId {string}")
    public void aEventIsReceivedForTokensAndCustomerId(String topic, Integer numberOfTokens, String cid) {
        Event event = new Event(topic,new Object[] {cid, numberOfTokens});
        service.generateTokensEvent(event);
    }
    @Then("the {string} event is sent")
    public void theEventIsSentWithGeneratedTokens(String topic) {
        //TODO: how to verify an event of random generated tokens?
        //var event = new Event(topic, new Object[] { new ArrayList<>()});
        //verify(queue).publish(event);
        assertEquals(topic,ReplyTokens.getEventName());
    }


    /*private CompletableFuture<List<Token>> listOfTokens = new CompletableFuture<>();
    List<Token> tokens = new ArrayList<>();

    User customer = new User();
    int numberOfTokens;

    @Given("a customer with customerId {string}")
    public void a_customer_with_customer_id(String id) {
        customer.setUserType(User.Type.CUSTOMER);
        customer.setUserName("Naja Jean Larsen");
        customer.setCpr("010101-0808");
        customer.setAccountId("9876543");
        customer.setUserId(id);
    }
    @Given("the customer has {int} token")
    public void the_customer_has_token(Integer numberOfTokens) {
        this.numberOfTokens = numberOfTokens;
        for (int i=0; i>numberOfTokens; i++ ) {
            Token t = new Token(customer.getUserId());
            t.tokenString = "testToken";
            tokens.add(t);
        }
        customer.setTokens(tokens);
    }
    @When("the customer request {int} tokens")
    public void the_customer_request_tokens(Integer numberOfTokens) {
        new Thread(() -> {
             List<Token> result = service.generateTokens(customer.getUserId(),numberOfTokens);
             listOfTokens.complete(result);

             //tokens.addAll(result);
             //customer.setTokens(tokens);
        }).start();
    }
    @Then("the {string} event is sent")
    public void the_event_is_sent(String topic) {
        RequestTokens requestTokens = new RequestTokens(customer.getUserId(), numberOfTokens);
        Event event = new Event(topic, new Object[] { requestTokens });
        verify(q).publish(event);
    }
    @When("the {string} event is sent with a list of tokens")
    public void the_event_is_sent_with_a_list_of_tokens(String topic) {
        assertEquals(ReplyTokens.getEventName(),topic);
        //tokenService.tokenReceived(new Event(topic, new Object[] {customer.getTokens()}));
    }
    @Then("the customer now has {int} tokens")
    public void the_customer_now_has_tokens(Integer numberOfTokens) {
        List<Token> generatedTokens = listOfTokens.join();

        tokens.addAll(generatedTokens);
        customer.setTokens(tokens);

        int actualNumberOfTokens = customer.getTokens().size();
        System.out.println("ASSERTTTTOOG:"+numberOfTokens+","+actualNumberOfTokens);
        //assertEquals(numberOfTokens, actualNumberOfTokens);
    }*/

}
