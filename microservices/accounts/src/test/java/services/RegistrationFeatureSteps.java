package services;

import event.BaseReplyEvent;
import event.account.RegisterUserReplied;
import event.account.RegisterUserRequested;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import org.junit.Assert;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegistrationFeatureSteps extends BaseTest {

    // arguments
    private String userBankAccount;
    private String cpr;
    private String name;
    private final UUID corr = UUID.randomUUID();

    @Given("there is a bank account with id {string}")
    public void thereIsABankAccountWithId(String accountId) {
        when(fakeBank.doesBankAccountExist(accountId)).thenReturn(true);
    }

    @When("event arrives requesting creation of customer with cpr {string}, name {string} and bankAccount {string}")
    public void eventArrivesRequestingCreationOfCustomerWithCprNameAndBankAccount(String cpr, String name, String userBankAccount) {
        this.userBankAccount = userBankAccount;
        this.cpr = cpr;
        this.name = name;

        registrationService.handleRegisterRequest(
                new Event(RegisterUserRequested.topic, new Object[]{
                        new RegisterUserRequested(corr, name, userBankAccount, cpr, false)
                })
        );
    }

    @Then("a customer is created and an event published")
    public void aCustomerIsCreatedWithCprNameAndBankAccount() {
        var found = database.findByCPR(cpr);
        Assert.assertNotNull(found);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(fakeMessageQueue).publish(captor.capture());
        Event actual = captor.getValue();

        var reply = actual.getArgument(0, RegisterUserReplied.class);
        Assert.assertEquals(corr, reply.getCorrelationId());
        Assert.assertTrue(reply.isSuccess());
        Assert.assertEquals(name, reply.getSuccessResponse().getName());
        Assert.assertEquals(cpr, reply.getSuccessResponse().getCpr());
        Assert.assertEquals(userBankAccount, reply.getSuccessResponse().getBankAccountId());

    }


    @Then("an error event is received with message {string}")
    public void anErrorEventIsReceivedWithMessage(String message) {
        verify(fakeMessageQueue).publish(new Event(
                RegisterUserReplied.topic,
                new Object[]{new RegisterUserReplied(
                        corr,
                        new BaseReplyEvent.SimpleFailure(message))
                }
        ));
    }
}
