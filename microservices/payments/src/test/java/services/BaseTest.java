package services;

import generated.dtu.ws.fastmoney.BankService;
import io.cucumber.java.Before;
import messaging.MessageQueue;
import services.services.PaymentService;
import team14messaging.ReplyWaiter;

import static org.mockito.Mockito.mock;

abstract class BaseTest {
    protected PaymentService paymentService;
    protected BankService mockBank;
    protected MessageQueue mockMessageQueue;
    protected ReplyWaiter mockWaiter;

    @Before
    public void initialize() {
        mockBank = mock(BankService.class);
        mockMessageQueue = mock(MessageQueue.class);
        mockWaiter = mock(ReplyWaiter.class);
        paymentService = new PaymentService(mockMessageQueue, mockBank, mockWaiter);
    }
}
