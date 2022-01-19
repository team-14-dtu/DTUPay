package behaviourtests;

import event.token.TokensReplied;
import event.token.TokensRequested;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import org.mockito.ArgumentCaptor;
import services.TokenManagementService;
import messaging.MessageQueue;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TokenGenerationSteps {
    private MessageQueue queue = mock(MessageQueue.class);
    private TokenManagementService service = new TokenManagementService(queue);
    private UUID correlationId;

    private TokensReplied reply;

    @Given("a customer with customerId {string} and {int} tokens")
    public void aCustomerWithCustomerIdAndTokens(String cid, int noOfTokens) {
        UUID uuidCid = UUID.nameUUIDFromBytes(cid.getBytes());
        int tokensForCidInDB = (service.tokenDatabase.get(uuidCid) == null) ? 0 : service.tokenDatabase.get(uuidCid).size();
        assertEquals(noOfTokens,tokensForCidInDB);
    }

    @When("a {string} event is received for {int} tokens and customerId {string}")
    public void aEventIsReceivedForTokensAndCustomerId(String topic, int noOfTokensRequested, String cid) {
        correlationId = UUID.randomUUID();

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
        ArgumentCaptor<Event> argument = ArgumentCaptor.forClass(Event.class);
        verify(queue).publish(argument.capture());
        reply = argument.getValue().getArgument(0,TokensReplied.class);

        assertEquals(TokensReplied.topic, argument.getValue().getType());
        assertEquals(TokensReplied.class, reply.getClass());
    }


    @Then("customerId {string} with now is associated with {int} tokens")
    public void customer_id_with_now_is_associated_with_tokens(String cid, int newNoOfTokens) {
        UUID uuidCid = UUID.nameUUIDFromBytes(cid.getBytes());
        assertEquals(newNoOfTokens,service.tokenDatabase.get(uuidCid).size());
    }

    @Then("an error message is received saying {string}")
    public void anErrorMessageIsReceivedSaying(String errorMessage) {
        assertEquals(errorMessage, reply.getFailureResponse().getReason());
    }
}
