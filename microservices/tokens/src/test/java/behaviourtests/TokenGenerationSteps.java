package behaviourtests;

import event.token.TokensReplied;
import event.token.TokensRequested;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import org.hamcrest.Matchers;
import org.mockito.Mockito;
import services.TokenManagementService;
import messaging.MessageQueue;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TokenGenerationSteps {
    private MessageQueue queue = mock(MessageQueue.class);
    private TokenManagementService service = new TokenManagementService(queue);
    private String correlationId;

    @Given("a customer with customerId {string} and {int} tokens")
    public void aCustomerWithCustomerIdAndTokens(String cid, int noOfTokens) {
        UUID uuidCid = UUID.nameUUIDFromBytes(cid.getBytes());
        int tokensForCidInDB = (service.tokenDatabase.get(uuidCid) == null) ? 0 : service.tokenDatabase.get(uuidCid).size();
        assertEquals(noOfTokens,tokensForCidInDB);
    }

    @When("a {string} event is received for {int} tokens and customerId {string}")
    public void aEventIsReceivedForTokensAndCustomerId(String topic, int noOfTokensRequested, String cid) {
        correlationId = UUID.randomUUID().toString();

        UUID uuidCid = UUID.nameUUIDFromBytes(cid.getBytes());
        Event event = new Event(topic,new Object[] {new TokensRequested(
                correlationId,
                uuidCid,
                noOfTokensRequested
        )});

        service.handleRequestTokens(event);
    }
    @Then("the {string} event is sent")
    public void theEventIsSentWithGeneratedTokens(String topic) {
        assertEquals(topic, TokensReplied.topic);


        verify(queue).publish((Event) Matchers.hasItem(Matchers.allOf(
                Matchers.<Event>hasProperty("type", is(TokensReplied.topic)),
                Matchers.<Event>hasProperty("arguments", contains(
                        Matchers.instanceOf(TokensReplied.class)
                ))
        )));




       /* verify(queue).publish(new Event(
                TokensReplied.topic,
                new Object[]{
                        new TokensReplied(
                                correlationId,
                                new TokensReplied.TokensRepliedSuccess(Mockito.anyList())
                        )
                }
        ));*/
    }

    @And("customerId {string} with now is associated with {int} tokens")
    public void customeridWithNowIsAssociatedWithTokens(String cid, int newNoOfTokens) {
        UUID uuidCid = UUID.nameUUIDFromBytes(cid.getBytes());
        assertEquals(newNoOfTokens,service.tokenDatabase.get(uuidCid).size());
    }

    @And("an error message is received saying {string}")
    public void anErrorMessageIsReceivedSaying(String errorMessage) {

    }
}
