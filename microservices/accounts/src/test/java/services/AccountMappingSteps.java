package services;
// @author : Emmanuel
import event.account.BankAccountIdFromCustomerIdReplied;
import event.account.BankAccountIdFromCustomerIdRequested;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import org.junit.Assert;
import org.mockito.ArgumentCaptor;
import services.adapters.db.Database;

import java.util.UUID;

import static org.mockito.Mockito.verify;

public class AccountMappingSteps extends BaseTest {

    private final String cpr = "11123";
    private final String name = "John";
    private UUID id;
    private UUID correlationId;

    @Given("a customer with a bank account ID {string}")
    public void aCustomerWithIDAndBankAccountID(String bankAccountId) throws Database.DatabaseError {
        id = database.save(name, cpr, bankAccountId).id;
    }

    @When("an event with his customer ID arrives")
    public void anEventWithCustomerIDArrives() {
        correlationId = UUID.randomUUID();
        registrationService.handleBankAccountIdFromCustomerId(
                new Event(BankAccountIdFromCustomerIdRequested.topic,
                        new Object[]{
                                new BankAccountIdFromCustomerIdRequested(
                                        correlationId,
                                        new BankAccountIdFromCustomerIdRequested.BRSuccess(id)
                                )}));
    }

    @Then("an event with his customer ID and bank account ID {string} is published")
    public void anEventWithCustomerIDAndBankAccountIDIsPublished(String bankAccountId) {
        verify(fakeMessageQueue).publish(new Event(
                BankAccountIdFromCustomerIdReplied.topic,
                new Object[]{new BankAccountIdFromCustomerIdReplied(
                        correlationId,
                        new BankAccountIdFromCustomerIdReplied.Success(
                                id, bankAccountId, name))}
        ));
    }

    @When("an event with random customer ID arrives")
    public void anEventWithRandomCustomerIDArrives() {
        correlationId = UUID.randomUUID();
        registrationService.handleBankAccountIdFromCustomerId(
                new Event(BankAccountIdFromCustomerIdRequested.topic,
                        new Object[]{
                                new BankAccountIdFromCustomerIdRequested(
                                        correlationId,
                                        new BankAccountIdFromCustomerIdRequested.BRSuccess(
                                                UUID.randomUUID()
                                        )

                                )}));
    }

    @When("an event with unknown customer ID arrives")
    public void anEventWithUnknownCustomerIDArrives() {
        correlationId = UUID.randomUUID();
        registrationService.handleBankAccountIdFromCustomerId(
                new Event(
                        BankAccountIdFromCustomerIdRequested.topic,
                        new Object[]{
                                new BankAccountIdFromCustomerIdRequested(
                                        correlationId,
                                        new BankAccountIdFromCustomerIdRequested.BRFailure("User not found")
                                )
                        }
                )
        );
    }

    @Then("an event with error message {string} is published")
    public void anEventWithErrorMessageIsPublished(String message) {
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(fakeMessageQueue).publish(captor.capture());
        Event actual = captor.getValue();
        var reply = actual.getArgument(0, BankAccountIdFromCustomerIdReplied.class);
        Assert.assertFalse(reply.isSuccess());

        Assert.assertEquals(correlationId, reply.getCorrelationId());
        Assert.assertEquals(message, reply.getFailureResponse().getReason());
    }
}
