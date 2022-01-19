package dk.dtu.team14;

import event.account.BankAccountIdFromCustomerIdReplied;
import event.account.BankAccountIdFromCustomerIdRequested;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;

import java.util.UUID;

import static org.mockito.Mockito.verify;

public class AccountMappingSteps extends BaseTest {

    private UUID id;
    private final String cpr = "11123";
    private final String name = "John";

    private final UUID correlationId = UUID.randomUUID();

    @Given("a customer with a bank account ID {string}")
    public void aCustomerWithIDAndBankAccountID(String bankAccountId) {
        id = database.save(name, cpr, bankAccountId).id;
    }

    @When("an event with his customer ID arrives")
    public void anEventWithCustomerIDArrives() {
        registrationService.handleBankAccountIdFromCustomerId(
                new Event(BankAccountIdFromCustomerIdRequested.topic,
                        new Object[]{
                                new BankAccountIdFromCustomerIdRequested(
                                        correlationId,
                                        id
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
}
