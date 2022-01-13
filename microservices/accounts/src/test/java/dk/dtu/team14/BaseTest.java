package dk.dtu.team14;

import dk.dtu.team14.adapters.bank.Bank;
import dk.dtu.team14.adapters.db.Database;
import dk.dtu.team14.services.RegistrationService;
import io.cucumber.java.Before;
import messaging.MessageQueue;

import static org.mockito.Mockito.mock;

abstract class BaseTest {
    protected RegistrationService registrationService;
    protected Bank fakeBank;
    protected MessageQueue fakeMessageQueue;
    protected Database fakeDatabase;

    @Before
    public void initialize() {
        fakeBank = mock(Bank.class);
        fakeMessageQueue = mock(MessageQueue.class);
        fakeDatabase = mock(Database.class);
        registrationService = new RegistrationService(fakeMessageQueue, fakeDatabase, fakeBank);
    }
}
