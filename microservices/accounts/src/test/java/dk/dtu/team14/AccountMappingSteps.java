package dk.dtu.team14;

import dk.dtu.team14.entities.User;
import event.account.BankAccountIdFromCustomerIdReplied;
import event.account.BankAccountIdFromCustomerIdRequested;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountMappingSteps extends BaseTest {

    private UUID id;
    private String userBankAccount;
    private String cpr;
    private String name = "John";

    private final UUID correlationId = UUID.randomUUID();

    @Given("a customer with ID {string} and bank account ID {string}")
    public void aCustomerWithIDAndBankAccountID(String customerId, String bankAccountId) {
        this.id = UUID.fromString(customerId);
        this.userBankAccount = bankAccountId;
        when(fakeDatabase.findById(id)).thenReturn(new User(id, userBankAccount, name, "Smith"));
    }

    @When("an event with customer ID {string} arrives")
    public void anEventWithCustomerIDArrives(String customerId) {
        registrationService.handleBankAccountIdFromCustomerId(
                new Event(BankAccountIdFromCustomerIdRequested.topic,
                        new Object[]{
                                new BankAccountIdFromCustomerIdRequested(
                                        correlationId,
                                        UUID.fromString(customerId)
                                )}));
    }

    @Then("an event with customer ID {string} and bank account ID {string} is published")
    public void anEventWithCustomerIDAndBankAccountIDIsPublished(String customerId, String bankAccountId) {
        verify(fakeMessageQueue).publish(new Event(
                BankAccountIdFromCustomerIdReplied.topic,
                new Object[]{new BankAccountIdFromCustomerIdReplied(correlationId, new BankAccountIdFromCustomerIdReplied.Success(UUID.fromString(customerId),bankAccountId, name))}
        ));
    }
}
