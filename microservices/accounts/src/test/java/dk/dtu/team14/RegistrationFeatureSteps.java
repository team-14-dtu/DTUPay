package dk.dtu.team14;

import dk.dtu.team14.adapters.db.implementations.StupidSimpleInMemoryDB;
import dk.dtu.team14.fakes.FakeMessageQueue;
import dk.dtu.team14.services.RegistrationService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RegistrationFeatureSteps {

    private RegistrationService registrationService;

    @Before
    void initialize() {
        var fakeMessageQueue = new FakeMessageQueue();
        var database = new StupidSimpleInMemoryDB();

        registrationService = new RegistrationService(fakeMessageQueue, database, bank);
    }

    @Given("there is a bank account with id {string}")
    public void thereIsABankAccountWithId(String arg0) {
    }

    @When("event arrives requesting creation of customer with cpr {string}, name {string} and bankAccount {string}")
    public void eventArrivesRequestingCreationOfCustomerWithCprNameAndBankAccount(String arg0, String arg1, String arg2) {
    }

    @Then("a customer is created with cpr {string}, name {string} and bankAccount {string}")
    public void aCustomerIsCreatedWithCprNameAndBankAccount(String arg0, String arg1, String arg2) {
    }
}
