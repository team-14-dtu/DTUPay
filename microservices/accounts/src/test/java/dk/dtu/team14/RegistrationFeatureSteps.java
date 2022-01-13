package dk.dtu.team14;

import dk.dtu.team14.adapters.bank.Bank;
import dk.dtu.team14.adapters.db.Database;
import dk.dtu.team14.services.RegistrationService;
import event.account.ReplyRegisterUser;
import event.account.ReplyRegisterUserSuccess;
import event.account.RequestRegisterUser;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class RegistrationFeatureSteps {

    private RegistrationService registrationService;
    private Bank fakeBank;
    private MessageQueue fakeMessageQueue;
    private Database fakeDatabase;


    private String bankAccountId;
    private String cpr;
    private String name;

    @Before
    public void initialize() {
        fakeBank = mock(Bank.class);
        fakeMessageQueue = mock(MessageQueue.class);
        fakeDatabase = mock(Database.class);
        registrationService = new RegistrationService(fakeMessageQueue, fakeDatabase, fakeBank);
    }

    @Given("there is a bank account with id {string}")
    public void thereIsABankAccountWithId(String accountId) {
        when(fakeBank.checkBankAccountExist(accountId)).thenReturn(true);
    }

    @When("event arrives requesting creation of customer with cpr {string}, name {string} and bankAccount {string}")
    public void eventArrivesRequestingCreationOfCustomerWithCprNameAndBankAccount(String cpr, String name, String bankAccountId) {
        registrationService.handleRegistrationRequest(
                new Event(RequestRegisterUser.topic, new Object[]{
                        new RequestRegisterUser(name, bankAccountId, cpr, false)
                })
        );

        this.cpr = cpr;
        this.name = name;
        this.bankAccountId = bankAccountId;
    }

    @Then("a customer is created and an event published")
    public void aCustomerIsCreatedWithCprNameAndBankAccount() {
        verify(fakeDatabase).save(name, cpr, bankAccountId);
        verify(fakeMessageQueue).publish(new Event(
                ReplyRegisterUser.topic,
                new Object[]{new ReplyRegisterUser(
                        cpr,
                        new ReplyRegisterUserSuccess(
                                name,
                                bankAccountId,
                                cpr,
                                Mockito.anyString()
                        ),
                        null)}
        ));
    }
}
