package dk.dtu.team14;

import event.account.RetireUserReplied;
import event.account.RetireUserRequested;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RetirementFeatureSteps extends BaseTest {

    private String id;
    private String userBankAccount;
    private String cpr;
    private String name;

    private final String correlationId = "00f7061a-4a7a-494c-897a-f809b501637e";

    @Given("there is a customer with id {string} cpr {string}, name {string} and bankAccount {string}")
    public void thereIsACustomerWithIdCprNameAndBankAccount(String id, String cpr, String name, String bankAccount) {
        this.id = id;
        this.cpr = cpr;
        this.name = name;
        this.userBankAccount = bankAccount;

        when(fakeDatabase.retire(cpr)).thenReturn(true);
    }

    @When("event arrives requesting retirement of that user")
    public void eventArrivesRequestingRetirementOfThatUser() {
        registrationService.handleRetireRequest(new Event(
                RetireUserRequested.topic,
                new Object[]{
                        new RetireUserRequested(correlationId, cpr)
                }
        ));
    }

    @Then("costumer is deleted and event published")
    public void costumerIsDeletedAndEventPublished() {
        verify(fakeDatabase).retire(cpr);
        verify(fakeMessageQueue).publish(new Event(
                RetireUserReplied.topic,
                new Object[]{new RetireUserReplied(correlationId, true)}
        ));
    }
}
