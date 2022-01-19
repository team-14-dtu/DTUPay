package dk.dtu.team14;

import event.account.RetireUserReplied;
import event.account.RetireUserRequested;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import org.junit.Assert;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RetirementFeatureSteps extends BaseTest {

    private UUID id;
    private String userBankAccount;
    private String cpr;
    private String name;

    private final UUID correlationId = UUID.randomUUID();

    @Given("there is a customer with cpr {string}, name {string} and bankAccount {string}")
    public void thereIsACustomerWithIdCprNameAndBankAccount(String cpr, String name, String bankAccount) {
        this.cpr = cpr;
        this.name = name;
        this.userBankAccount = bankAccount;
        this.id = database.save(name, cpr, userBankAccount).id;
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

    @Then("user is deleted and event published")
    public void costumerIsDeletedAndEventPublished() {

        var found = database.findById(id);
        Assert.assertNull(found);

        verify(fakeMessageQueue).publish(new Event(
                RetireUserReplied.topic,
                new Object[]{new RetireUserReplied(correlationId,new RetireUserReplied.Success())}
        ));
    }
}
