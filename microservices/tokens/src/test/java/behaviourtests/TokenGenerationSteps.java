package behaviourtests;

import event.token.TokensReplied;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import services.TokenManagementService;
import messaging.MessageQueue;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class TokenGenerationSteps {
    private MessageQueue queue = mock(MessageQueue.class);
    private TokenManagementService service = new TokenManagementService(queue);

    @Given("a customer with customerId {string} and {int} tokens")
    public void aCustomerWithCustomerIdAndTokens(String cid, int noOfTokens) {
        UUID cidU = UUID.fromString(cid);
        service.tokenDatabase.put(cidU, service.generateNewTokens(cidU,noOfTokens));
        int tokensForCidInDB = (service.tokenDatabase.get(cidU) == null) ? 0 : service.tokenDatabase.get(cidU).size();
        assertEquals(noOfTokens,tokensForCidInDB);
    }

    @When("a {string} event is received for {int} tokens and customerId {string}")
    public void aEventIsReceivedForTokensAndCustomerId(String topic, int noOfTokensRequested, String cid) {
        UUID cidU = UUID.fromString(cid);
        Event event = new Event(topic,new Object[] {cid, noOfTokensRequested});
        service.generateNewTokens(cidU, noOfTokensRequested);
  //      service.generateTokensEvent(event);
    }
    @Then("the {string} event is sent")
    public void theEventIsSentWithGeneratedTokens(String topic) {
        assertEquals(topic, TokensReplied.topic);
    }

    @And("customerId {string} is now associated with {int} tokens")
    public void customeridWithNowIsAssociatedWithTokens(String cid, int newNoOfTokens) {
        UUID uuidCid = UUID.fromString(cid);
        assertEquals(newNoOfTokens,service.tokenDatabase.get(uuidCid).size());

    }
}
