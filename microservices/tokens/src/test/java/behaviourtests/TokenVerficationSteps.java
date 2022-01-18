package behaviourtests;

import event.account.BankAccountIdFromCustomerIdRequested;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import services.TokenManagementService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TokenVerficationSteps {
    private MessageQueue queue = mock(MessageQueue.class);
    private TokenManagementService service = new TokenManagementService(queue);
    UUID cidU;
    UUID tokenIdU;

    @Given("a customer with customerId {string} who is in possesion of a token with tokenId {string}")
    public void a_customer_with_customer_id(String cid, String tokenId) {
        tokenIdU = UUID.fromString(tokenId);
        cidU = UUID.fromString(cid);
        List<UUID> tokenList = new ArrayList<UUID>();
        tokenList.add(tokenIdU);
        service.tokenDatabase.put(cidU, tokenList);
    }

    @When("the {string} event is sent containing the tokenId")
    public void the_event_is_sent(String topic) {
        Event event = new Event(topic,new Object[] {tokenIdU});
    }
    @Then("the {string} event is received with the customerId")
    public void the_event_is_received_with_the_customer_id(String topic) {
        assertEquals(topic, BankAccountIdFromCustomerIdRequested.topic);

    }

}
