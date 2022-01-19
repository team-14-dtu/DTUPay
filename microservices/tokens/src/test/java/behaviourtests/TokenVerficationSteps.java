package behaviourtests;

import event.account.BankAccountIdFromCustomerIdRequested;
import event.token.CustomerIdFromTokenRequested;
import event.token.TokensReplied;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import services.TokenManagementService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TokenVerficationSteps {
    private MessageQueue queue = Mockito.mock(MessageQueue.class);
    private TokenManagementService service = new TokenManagementService(queue);
    private UUID cidU;
    private UUID tokenIdU;
    private UUID correlationId = UUID.randomUUID();

    @Given("a customer with customerId {string} who is in possession of a token with tokenId {string}")
    public void a_customer_with_customer_id(String cid, String tokenId) {
        tokenIdU = UUID.fromString(tokenId);
        cidU = UUID.fromString(cid);
        List<UUID> tokenList = new ArrayList<UUID>();
        tokenList.add(tokenIdU);
        service.tokenDatabase.put(cidU, tokenList);
        assertTrue(service.tokenDatabase.get(cidU).contains(tokenIdU));
    }

    @When("the {string} event is sent containing the tokenId")
    public void the_event_is_sent(String topic) {
        Event event = new Event(topic,new Object[] {
                new CustomerIdFromTokenRequested(
                        correlationId,
                        tokenIdU)});

        service.handleRequestCustomerIdFromToken(event);
    }

    @Then("the {string} event is received containing the customerId")
    public void the_event_is_received_with_the_customer_id(String topic) {
        assertEquals(topic, BankAccountIdFromCustomerIdRequested.topic);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        Mockito.verify(this.queue).publish(captor.capture());
        Event value = captor.getValue();

        assertEquals(cidU, value.getArgument(0,BankAccountIdFromCustomerIdRequested.class).getCustomerId());
    }

    @Given("an invalid tokenId {string}")
    public void aTokenWithTokenId(String arg0) {
        UUID inTokenId = UUID.fromString(arg0);
    }

    @Then("the {string} event is received containing an error-message saying {string}")
    public void theEventIsReceivedContainingAnErrorMessageSaying(String topic, String response) {
        assertEquals(topic, BankAccountIdFromCustomerIdRequested.topic);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        Mockito.verify(this.queue).publish(captor.capture());
        Event value = captor.getValue();

        assertEquals(response,value.getArgument(0,BankAccountIdFromCustomerIdRequested.class).getFailResponse().getMessage());

    }
}
