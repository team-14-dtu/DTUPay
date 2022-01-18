package dk.dtu.team14;

import dk.dtu.team14.entities.User;
import event.account.RegisterUserReplied;
import event.account.RegisterUserRepliedFailure;
import event.account.RegisterUserRepliedSuccess;
import event.account.RegisterUserRequested;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class RegistrationFeatureSteps extends BaseTest {

    // arguments
    private String userBankAccount;
    private String cpr;
    private String name;
    private UUID corr = UUID.randomUUID();
    // generated
    private UUID id;

    @Given("there is a bank account with id {string} and we want to create a customer with cpr {string}, name {string} and bankAccount {string}")
    public void thereIsABankAccountWithId(String accountId, String cpr, String name, String userBankAccount) {
        when(fakeBank.doesBankAccountExist(accountId)).thenReturn(accountId.equals(userBankAccount));

        id = UUID.randomUUID();
        when(fakeDatabase.save(name, cpr, userBankAccount)).thenReturn(
                new User(id, userBankAccount, name, cpr)
        );

        this.cpr = cpr;
        this.name = name;
        this.userBankAccount = userBankAccount;
    }

    @When("event arrives requesting creation")
    public void eventArrivesRequestingCreationOfCustomerWithCprNameAndBankAccount() {
        registrationService.handleRegisterRequest(
                new Event(RegisterUserRequested.topic, new Object[]{
                        new RegisterUserRequested(corr, name, userBankAccount, cpr, false)
                })
        );
    }

    @Then("a customer is created and an event published")
    public void aCustomerIsCreatedWithCprNameAndBankAccount() {
        verify(fakeDatabase).save(name, cpr, userBankAccount);
        verify(fakeMessageQueue).publish(new Event(
                RegisterUserReplied.topic,
                new Object[]{new RegisterUserReplied(
                        corr,
                        cpr,
                        new RegisterUserRepliedSuccess(
                                name,
                                userBankAccount,
                                cpr,
                                id
                        ),
                        null)}
        ));
    }


    @Then("an error event is received with message {string}")
    public void anErrorEventIsReceivedWithMessage(String message) {
        verify(fakeMessageQueue).publish(new Event(
                RegisterUserReplied.topic,
                new Object[]{new RegisterUserReplied(
                        corr,
                        cpr,
                        null,
                        new RegisterUserRepliedFailure(message))}
        ));
    }
}
