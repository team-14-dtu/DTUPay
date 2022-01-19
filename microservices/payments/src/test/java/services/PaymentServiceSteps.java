package services;

import event.account.*;
import event.payment.history.PaymentHistoryReplied;
import event.payment.history.PaymentHistoryRequested;
import event.payment.pay.PayReplied;
import event.payment.pay.PayRequested;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import org.junit.Assert;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import services.data.Payment;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.verify;

public class PaymentServiceSteps extends BaseTest {

    private UUID tokenId;
    private UUID merchantId;
    private BigDecimal amount;
    private String description;
    private UUID correlationId;
    private UUID paymentId;
    private UUID customerId;
//    private BankAccountIdFromMerchantIdRequested bankAccountIdFromMerchantIdRequested;
//    private BankAccountIdFromCustomerIdRequested bankAccountIdFromCustomerIdRequested;
    private PayReplied payReplied;

    @Given("a valid token with id {string}")
    public void a_valid_token_with_id(String tokenId) {
        this.tokenId = UUID.fromString(tokenId);
    }
    @Given("a merchant with id {string}")
    public void a_merchant_with_id(String merchantId) {
        this.merchantId = UUID.fromString(merchantId);
    }
    @Given("an amount of {int} with description {string}")
    public void an_amount_of_with_description(Integer amount, String description) {
        this.amount = BigDecimal.valueOf(amount);
        this.description = description;
    }
    @When("an event arrives requesting payment")
    public void an_event_arrives_requesting_payment() throws ExecutionException, InterruptedException {

        Mockito.when(mockWaiter.synchronouslyWaitForReply(Mockito.any())).thenReturn(
                new Event(BankAccountIdFromMerchantIdReplied.topic, new Object[]{
                        new BankAccountIdFromMerchantIdReplied(
                                UUID.randomUUID(),
                                new BankAccountIdFromMerchantIdReplied.Success()
                        )
                }
                ), new Event(BankAccountIdFromCustomerIdReplied.topic, new Object[]{
                        new BankAccountIdFromCustomerIdReplied(
                                UUID.randomUUID(),
                                new BankAccountIdFromCustomerIdReplied.Success()
                        )
                }
                )
        );



        Mockito.when(mockWaiter.synchronouslyWaitForReply(Mockito.any())).thenReturn(
                new Event(BankAccountIdFromMerchantIdReplied.topic, new Object[]{
                        new BankAccountIdFromMerchantIdReplied(
                                UUID.randomUUID(),
                                new BankAccountIdFromMerchantIdReplied.Success()
                        )
                }
        ), new Event(BankAccountIdFromCustomerIdReplied.topic, new Object[]{
                        new BankAccountIdFromCustomerIdReplied(
                                UUID.randomUUID(),
                                new BankAccountIdFromCustomerIdReplied.Success()
                        )
                }
        )
        );

        correlationId = UUID.randomUUID();
        paymentService.handlePayRequest(
                new Event(PayRequested.topic,
                        new Object[]{
                                new PayRequested(
                                        correlationId,
                                        tokenId,
                                        merchantId,
                                        amount,
                                        description
                                )}));

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(mockMessageQueue, Mockito.times(3)).publish(captor.capture());
        List<Event> actualAll = captor.getAllValues();
//        this.bankAccountIdFromMerchantIdRequested = actualAll.get(0).getArgument(0, BankAccountIdFromMerchantIdRequested.class);
//        this.bankAccountIdFromCustomerIdRequested = actualAll.get(1).getArgument(0, BankAccountIdFromCustomerIdRequested.class);
        this.payReplied = actualAll.get(2).getArgument(0, PayReplied.class);
    }
    @Then("a payment is registered and an event is published")
    public void a_payment_is_registered_and_an_event_is_published() {
        Assert.assertEquals(correlationId, payReplied.getCorrelationId());
        Assert.assertEquals(amount, payReplied.getSuccessResponse().getAmount());
        Assert.assertEquals(description, payReplied.getSuccessResponse().getDescription());
    }

    @Given("a customer with id {string}")
    public void a_customer_with_id(String customerId) {
        this.customerId = UUID.fromString(customerId);
    }

    @Given("a payment exists for the customer")
    public void a_payment_exists_for_the_customer() {
        Payment payment = new Payment(customerId, UUID.randomUUID(), BigDecimal.valueOf(100), "Receipt: Books", Timestamp.valueOf("2022-01-20 08:59:59.0"), "Keanu Reeves", "Samuel L. Jackson");
        paymentService.getPaymentHistory().addPaymentHistory(UUID.randomUUID(), payment);
    }

    @When("an event arrives requesting the customers payment history")
    public void an_event_arrives_requesting_the_customers_payment_history() {
        correlationId = UUID.randomUUID();
        paymentService.handlePaymentCustomerHistoryRequest(
                new Event(PaymentHistoryRequested.PaymentCustomerHistoryRequested.topic, new Object[]{
                        new PaymentHistoryRequested.PaymentCustomerHistoryRequested(correlationId, customerId)
                })
        );
    }
    @Then("the customer payment history is fetched from the payment database and an event is published")
    public void the_customer_payment_history_is_fetched_from_the_payment_database_and_an_event_is_published() {
        var customerPaymentHistory = paymentService.getPaymentHistory().getCustomerHistory(customerId);
        Assert.assertNotNull(customerPaymentHistory);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(mockMessageQueue).publish(captor.capture());
        Event actual = captor.getValue();

        var reply = actual.getArgument(0, PaymentHistoryReplied.PaymentCustomerHistoryReplied.class);
        Assert.assertEquals(correlationId, reply.getCorrelationId());
        Assert.assertEquals(customerPaymentHistory, reply.getSuccessResponse().getCustomerHistoryList());
    }

    @Given("a payment exists for the merchant")
    public void a_payment_exists_for_the_merchant() {
        Payment payment = new Payment(UUID.randomUUID(), merchantId, BigDecimal.valueOf(100), "Receipt: Eggs", Timestamp.valueOf("2016-02-03 00:00:00.0"), "Leonardo Da Vinci", "Michael Jackson");
        paymentService.getPaymentHistory().addPaymentHistory(UUID.randomUUID(), payment);
    }
    @When("an event arrives requesting the merchants payment history")
    public void an_event_arrives_requesting_the_merchants_payment_history() {
        correlationId = UUID.randomUUID();
        paymentService.handlePaymentMerchantHistoryRequest(
                new Event(PaymentHistoryRequested.PaymentMerchantHistoryRequested.topic, new Object[]{
                        new PaymentHistoryRequested.PaymentMerchantHistoryRequested(correlationId, merchantId)
                })
        );
    }
    @Then("the merchant payment history is fetched from the payment database and an event is published")
    public void the_merchant_payment_history_is_fetched_from_the_payment_database_and_an_event_is_published() {
        var merchantPaymentHistory = paymentService.getPaymentHistory().getMerchantHistory(merchantId);
        Assert.assertNotNull(merchantPaymentHistory);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(mockMessageQueue).publish(captor.capture());
        Event actual = captor.getValue();

        var reply = actual.getArgument(0, PaymentHistoryReplied.PaymentMerchantHistoryReplied.class);
        Assert.assertEquals(correlationId, reply.getCorrelationId());
        Assert.assertEquals(merchantPaymentHistory, reply.getSuccessResponse().getMerchantHistoryList());
    }

    @Given("a payment exists in the payment database")
    public void a_payment_exists_in_the_payment_database() {
        Payment payment = new Payment(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(100), "Receipt: Eggs", Timestamp.valueOf("2019-04-11 12:00:00.0"), "John Johnson", "Fat Albert");
        paymentService.getPaymentHistory().addPaymentHistory(UUID.randomUUID(), payment);
    }
    @When("an event arrives requesting the managers payment history")
    public void an_event_arrives_requesting_the_managers_payment_history() {
        correlationId = UUID.randomUUID();
        paymentService.handlePaymentManagerHistoryRequest(
                new Event(PaymentHistoryRequested.PaymentManagerHistoryRequested.topic, new Object[]{
                        new PaymentHistoryRequested.PaymentManagerHistoryRequested(correlationId)
                })
        );
    }
    @Then("the manager payment history is fetched from the payment database and an event is published")
    public void the_manager_payment_history_is_fetched_from_the_payment_database_and_an_event_is_published() {
        var managerPaymentHistory = paymentService.getPaymentHistory().getManagerHistory();
        Assert.assertNotNull(managerPaymentHistory);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(mockMessageQueue).publish(captor.capture());
        Event actual = captor.getValue();

        var reply = actual.getArgument(0, PaymentHistoryReplied.PaymentManagerHistoryReplied.class);
        Assert.assertEquals(correlationId, reply.getCorrelationId());
        Assert.assertEquals(managerPaymentHistory, reply.getSuccessResponse().getManagerHistoryList());
    }

}
