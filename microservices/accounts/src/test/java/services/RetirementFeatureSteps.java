package services;
// @author : Petr
import event.BaseReplyEvent;
import event.account.RetireUserReplied;
import event.account.RetireUserRequested;
import event.account.UserExistsReplied;
import event.account.UserExistsRequested;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import org.junit.Assert;
import org.mockito.ArgumentCaptor;
import services.adapters.db.Database;

import java.util.UUID;

import static org.mockito.Mockito.verify;

public class RetirementFeatureSteps extends BaseTest {

    private UUID id;
    private String userBankAccount;
    private String cpr;
    private String name;

    private UUID correlationId = UUID.randomUUID();

    @Given("there is a customer with cpr {string}, name {string} and bankAccount {string}")
    public void thereIsACustomerWithIdCprNameAndBankAccount(String cpr, String name, String bankAccount) throws Database.DatabaseError {
        this.cpr = cpr;
        this.name = name;
        this.userBankAccount = bankAccount;
        this.id = database.save(name, cpr, userBankAccount).id;
    }

    @When("event arrives requesting retirement of that user")
    public void eventArrivesRequestingRetirementOfThatUser() {

        correlationId = UUID.randomUUID();
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
                new Object[]{new RetireUserReplied(correlationId, new RetireUserReplied.Success())}
        ));

    }

    @Then("there is an error message saying {string}")
    public void thereIsAnErrorMessage(String errorMessage) {

        var found = database.findById(id);
        Assert.assertNull(found);

        verify(fakeMessageQueue).publish(new Event(
                RetireUserReplied.topic,
                new Object[]{new RetireUserReplied(correlationId, new BaseReplyEvent.SimpleFailure(errorMessage))}
        ));
    }

    @Given("there is a non-customer with cpr {string}, name {string} and bankAccount {string}")
    public void thereIsANonCustomerWithCprNameAndBankAccount(String cpr, String name, String bankAccount) {
        this.cpr = cpr;
        this.name = name;
        this.userBankAccount = bankAccount;
    }

    @When("event arrives checking if the user exists in DTUPay")
    public void event_arrives_checking_if_the_user_exists_in_dtu_pay() {
        registrationService.handleUserExistsRequest(
                new Event(
                        UserExistsRequested.topic,
                        new Object[]{
                                new UserExistsRequested(
                                        correlationId,
                                        this.id
                                )
                        }
                )
        );
    }

    @Then("a succeeding event is published")
    public void a_succeeding_event_is_published() {
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(fakeMessageQueue).publish(captor.capture());
        Event actual = captor.getValue();
        var reply = actual.getArgument(0, UserExistsReplied.class);
        Assert.assertTrue(reply.isSuccess());
    }

    @Given("a non existing customer")
    public void a_non_existing_customer() {
        this.id = UUID.randomUUID();
    }

    @Then("a failing event is published")
    public void a_failing_event_is_published() {
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(fakeMessageQueue).publish(captor.capture());
        Event actual = captor.getValue();
        var reply = actual.getArgument(0, UserExistsReplied.class);
        Assert.assertTrue(!reply.isSuccess());
    }

}
