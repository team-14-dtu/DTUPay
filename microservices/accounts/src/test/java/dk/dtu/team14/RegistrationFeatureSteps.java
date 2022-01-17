package dk.dtu.team14;

import dk.dtu.team14.adapters.bank.Bank;
import dk.dtu.team14.adapters.db.Database;
import dk.dtu.team14.entities.User;
import dk.dtu.team14.services.RegistrationService;
import event.account.ReplyRegisterUser;
import event.account.ReplyRegisterUserFailure;
import event.account.ReplyRegisterUserSuccess;
import event.account.RequestRegisterUser;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class RegistrationFeatureSteps extends BaseTest {

    // arguments
    private String userBankAccount;
    private String cpr;
    private String name;

    // generated
    private String id;

    @Given("there is a bank account with id {string} and we want to create a customer with cpr {string}, name {string} and bankAccount {string}")
    public void thereIsABankAccountWithId(String accountId, String cpr, String name, String userBankAccount) {
        when(fakeBank.doesBankAccountExist(accountId)).thenReturn(accountId.equals(userBankAccount));

        id = UUID.randomUUID().toString();
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
                new Event(RequestRegisterUser.topic, new Object[]{
                        new RequestRegisterUser("123", name, userBankAccount, cpr, false)
                })
        );
    }

    @Then("a customer is created and an event published")
    public void aCustomerIsCreatedWithCprNameAndBankAccount() {
        verify(fakeDatabase).save(name, cpr, userBankAccount);
        verify(fakeMessageQueue).publish(new Event(
                ReplyRegisterUser.topic,
                new Object[]{new ReplyRegisterUser(
                        "123",
                        cpr,
                        new ReplyRegisterUserSuccess(
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
                ReplyRegisterUser.topic,
                new Object[]{new ReplyRegisterUser(
                        "123",
                        cpr,
                        null,
                        new ReplyRegisterUserFailure(message))}
        ));
    }
}
