package dk.dtu.team14;

import dk.dtu.team14.adapters.bank.Bank;
import dk.dtu.team14.adapters.db.Database;
import dk.dtu.team14.adapters.db.implementations.StupidSimpleInMemoryDB;
import dk.dtu.team14.services.RegistrationService;
import io.cucumber.java.Before;
import messaging.MessageQueue;
import team14messaging.SimpleQueue;

import static org.mockito.Mockito.mock;

abstract class BaseTest {
    protected RegistrationService registrationService;
    protected Bank fakeBank;
    protected MessageQueue fakeMessageQueue;
    protected Database database;

    @Before
    public void initialize() {
        fakeBank = mock(Bank.class);
        fakeMessageQueue = mock(MessageQueue.class);
        database = new StupidSimpleInMemoryDB();
        database.clear(); // Users are in a static field, so new does not help

        registrationService = new RegistrationService(
                new SimpleQueue(fakeMessageQueue),
                database,
                fakeBank
        );
    }
}
