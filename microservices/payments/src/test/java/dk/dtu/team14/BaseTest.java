package dk.dtu.team14;

import dk.dtu.team14.services.PaymentService;
import io.cucumber.java.Before;
import messaging.MessageQueue;

import static org.mockito.Mockito.mock;

public abstract class BaseTest {
    protected PaymentService paymentService;
    protected MessageQueue fakeMessageQueue;

    @Before
    public void initialize() {
        fakeMessageQueue = mock(MessageQueue.class);
        paymentService = new PaymentService();
    }
}
