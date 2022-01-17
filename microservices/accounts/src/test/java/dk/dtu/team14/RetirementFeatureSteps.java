package dk.dtu.team14;

import event.account.ReplyRetireUser;
import event.account.RequestRetireUser;
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


    @Given("there is a customer with id {string} cpr {string}, name {string} and bankAccount {string}")
    public void thereIsACustomerWithIdCprNameAndBankAccount(String id, String cpr, String name, String bankAccount) {
        this.id = id;
        this.cpr = cpr;
        this.name = name;
        this.userBankAccount = bankAccount;

        when(fakeDatabase.retire(id)).thenReturn(true);
    }

    @When("event arrives requesting retirement of that user")
    public void eventArrivesRequestingRetirementOfThatUser() {
        registrationService.handleRetireRequest(new Event(
                RequestRetireUser.topic,
                new Object[]{
                        new RequestRetireUser(id)
                }
        ));
    }

    @Then("costumer is deleted and event published")
    public void costumerIsDeletedAndEventPublished() {
        verify(fakeDatabase).retire(id);
        verify(fakeMessageQueue).publish(new Event(
                ReplyRetireUser.topic,
                new Object[]{new ReplyRetireUser(id, true)}
        ));
    }


}
