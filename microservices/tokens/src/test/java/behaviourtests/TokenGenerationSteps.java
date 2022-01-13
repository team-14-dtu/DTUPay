package behaviourtests;

import event.token.ReplyTokens;
import event.token.RequestTokens;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import services.TokenManagementService;
import messaging.MessageQueue;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class TokenGenerationSteps {
    private MessageQueue queue = mock(MessageQueue.class);
    private TokenManagementService service = new TokenManagementService(queue);

    @Given("a customer with customerId {string} and {int} tokens")
    public void aCustomerWithCustomerIdAndTokens(String cid, int noOfTokens) {
        int tokensForCidInDB = (service.tokenDatabase.get(cid) == null) ? 0 : service.tokenDatabase.get(cid).size();
        assertEquals(noOfTokens,tokensForCidInDB);
    }

    @When("a {string} event is received for {int} tokens and customerId {string}")
    public void aEventIsReceivedForTokensAndCustomerId(String topic, int noOfTokensRequested, String cid) {
        Event event = new Event(topic,new Object[] {cid, noOfTokensRequested});
        service.generateTokensEvent(event);
    }
    @Then("the {string} event is sent")
    public void theEventIsSentWithGeneratedTokens(String topic) {
        assertEquals(topic,ReplyTokens.getEventName());
    }

    @And("customerId {string} with now is associated with {int} tokens")
    public void customeridWithNowIsAssociatedWithTokens(String cid, int newNoOfTokens) {
        assertEquals(newNoOfTokens,service.tokenDatabase.get(cid).size());
    }
}
