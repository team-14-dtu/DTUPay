package behaviourtests;

import event.token.TokensReplied;
import event.token.TokensRequested;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import org.mockito.ArgumentCaptor;
import services.TokenManagementService;
import services.db.implementations.StupidSimpleInMemoryDB;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TokenGenerationSteps {
    private MessageQueue queue = mock(MessageQueue.class);
    private StupidSimpleInMemoryDB db = new StupidSimpleInMemoryDB();
    private TokenManagementService service = new TokenManagementService(queue, db);
    private UUID correlationId;

    private TokensReplied reply;


    @Given("a customer with customerId {string} and {int} tokens")
    public void aCustomerWithCustomerIdAndTokens(String cid, int noOfTokens) {
        UUID uuidCid = UUID.nameUUIDFromBytes(cid.getBytes());

        //Simulate customers tokens
        List<UUID> existingTokens = new ArrayList<>();
        for (int i = 0; i < noOfTokens; i++) {
            existingTokens.add(UUID.randomUUID());
        }
        db.addTokens(uuidCid, existingTokens);

        assertEquals(noOfTokens, service.database.getTokens(uuidCid).size());
    }

    @When("a {string} event is received for {int} tokens and customerId {string}")
    public void aEventIsReceivedForTokensAndCustomerId(String topic, int noOfTokensRequested, String cid) {
        correlationId = UUID.randomUUID();

        UUID uuidCid = UUID.nameUUIDFromBytes(cid.getBytes());
        Event event = new Event(topic, new Object[]{new TokensRequested(
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
        reply = argument.getValue().getArgument(0, TokensReplied.class);

        assertEquals(TokensReplied.topic, argument.getValue().getType());
        assertEquals(TokensReplied.class, reply.getClass());
    }


    @Then("customerId {string} with now is associated with {int} tokens")
    public void customer_id_with_now_is_associated_with_tokens(String cid, int newNoOfTokens) {
        UUID uuidCid = UUID.nameUUIDFromBytes(cid.getBytes());
        assertEquals(newNoOfTokens, service.database.getTokens(uuidCid).size());
    }

    @Then("an error message is received saying {string}")
    public void anErrorMessageIsReceivedSaying(String errorMessage) {
        assertEquals(errorMessage, reply.getFailureResponse().getReason());
    }
}
