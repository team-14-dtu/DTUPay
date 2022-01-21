package services;

import event.BaseReplyEvent;
import event.account.BankAccountIdFromCustomerIdReplied;
import event.account.BankAccountIdFromMerchantIdReplied;
import event.account.UserExistsReplied;
import event.payment.history.PaymentHistoryReplied;
import event.payment.history.PaymentHistoryRequested;
import event.payment.pay.PayReplied;
import event.payment.pay.PayRequested;
import generated.dtu.ws.fastmoney.Account;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import rest.PaymentHistoryCustomer;
import rest.PaymentHistoryManager;
import rest.PaymentHistoryMerchant;
import services.data.Payment;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;

public class PaymentServiceSteps extends BaseTest {
    List<PaymentHistoryCustomer> customerPaymentHistory = new ArrayList<>();
    List<PaymentHistoryMerchant> merchantPaymentHistory = new ArrayList<>();
    List<PaymentHistoryManager> managerPaymentHistory = new ArrayList<>();
    private UUID tokenId;
    private UUID merchantId;
    private BigDecimal amount;
    private String description;
    private UUID correlationId;
    private UUID customerId;
    private PayReplied payReplied;

    private static boolean matchesCustomerHistory(PaymentHistoryCustomer p1, PaymentHistoryCustomer p2) {
        return p1.getAmount().equals(p2.getAmount()) &&
                p1.getMerchantName().equals(p2.getMerchantName()) &&
                p1.getDescription().equals(p2.getDescription()) &&
                p1.getTimestamp().equals(p2.getTimestamp());
    }

    private static boolean matchesMerchantHistory(PaymentHistoryMerchant p1, PaymentHistoryMerchant p2) {
        return p1.getAmount().equals(p2.getAmount()) &&
                p1.getDescription().equals(p2.getDescription()) &&
                p1.getTimestamp().equals(p2.getTimestamp());
    }

    private static boolean matchesManagerHistory(PaymentHistoryManager p1, PaymentHistoryManager p2) {
        return p1.getAmount().equals(p2.getAmount()) &&
                p1.getMerchantName().equals(p2.getMerchantName()) &&
                p1.getDescription().equals(p2.getDescription()) &&
                p1.getTimestamp().equals(p2.getTimestamp()) &&
                p1.getCustomerName().equals(p2.getCustomerName()) &&
                p1.getMerchantId().equals(p2.getMerchantId()) &&
                p1.getCustomerId().equals(p2.getCustomerId());
    }

    @Before
    public void clearHistory() {
        customerPaymentHistory = new ArrayList<>();
        merchantPaymentHistory = new ArrayList<>();
        managerPaymentHistory = new ArrayList<>();
    }

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
    public void an_event_arrives_requesting_payment() throws BankServiceException_Exception {
        Account account = Mockito.mock(Account.class);
        Mockito.when(mockBank.getAccount(Mockito.any())).thenReturn(account);
        Mockito.when(account.getBalance()).thenReturn(amount);
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
                ));

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
    }

    @Then("a payment is registered and an event is published")
    public void a_payment_is_registered_and_an_event_is_published() {
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(mockMessageQueue, Mockito.times(3)).publish(captor.capture());
        List<Event> actualAll = captor.getAllValues();
        this.payReplied = actualAll.get(2).getArgument(0, PayReplied.class);

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
        BigDecimal amount = BigDecimal.valueOf(100);
        String description = "Receipt: Books";
        Timestamp timestamp = Timestamp.valueOf("2022-01-20 08:59:59.0");
        String merchantName = "Samuel L. Jackson";
        Payment payment = new Payment(customerId, UUID.randomUUID(), amount, description, timestamp, "Keanu Reeves", merchantName);
        customerPaymentHistory.add(new PaymentHistoryCustomer(UUID.randomUUID(), amount, description, timestamp, merchantName));
        paymentService.getPaymentHistory().addPaymentHistory(UUID.randomUUID(), payment);
    }

    @When("an event arrives requesting the customers payment history")
    public void an_event_arrives_requesting_the_customers_payment_history() {
        correlationId = UUID.randomUUID();

        Mockito.when(mockWaiter.synchronouslyWaitForReply(Mockito.any())).thenReturn(
                new Event(UserExistsReplied.topic, new Object[]{
                        new UserExistsReplied(
                                UUID.randomUUID(),
                                new UserExistsReplied.Success()
                        )
                }
                ));

        paymentService.handlePaymentCustomerHistoryRequest(
                new Event(PaymentHistoryRequested.PaymentCustomerHistoryRequested.topic, new Object[]{
                        new PaymentHistoryRequested.PaymentCustomerHistoryRequested(correlationId, customerId)
                })
        );
    }

    @Then("the customer payment history is fetched from the payment database and an event is published")
    public void the_customer_payment_history_is_fetched_from_the_payment_database_and_an_event_is_published() {
        Assert.assertNotNull(customerPaymentHistory);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(mockMessageQueue, Mockito.times(2)).publish(captor.capture());
        Event actual = captor.getAllValues().get(1);

        var reply = actual.getArgument(0, PaymentHistoryReplied.PaymentCustomerHistoryReplied.class);
        Assert.assertEquals(correlationId, reply.getCorrelationId());
        boolean equalItems = true;
        var actualHistory = reply.getSuccessResponse().getCustomerHistoryList();
        for (PaymentHistoryCustomer i : customerPaymentHistory) {
            boolean containsEquivalent = false;
            for (PaymentHistoryCustomer j : actualHistory) {
                if (matchesCustomerHistory(i, j)) {
                    containsEquivalent = true;
                    break;
                }
            }
            if (!containsEquivalent) {
                equalItems = false;
                break;
            }
        }
        Assert.assertTrue(equalItems);
    }

    @Given("a payment exists for the merchant")
    public void a_payment_exists_for_the_merchant() {
        BigDecimal amount = BigDecimal.valueOf(100);
        String description = "Receipt: Eggs";
        Timestamp timestamp = Timestamp.valueOf("2032-01-20 08:59:59.0");
        Payment payment = new Payment(UUID.randomUUID(), merchantId, amount, description, timestamp, "Leonardo Da Vinci", "Michael Jackson");
        merchantPaymentHistory.add(new PaymentHistoryMerchant(UUID.randomUUID(), amount, description, timestamp));
        paymentService.getPaymentHistory().addPaymentHistory(UUID.randomUUID(), payment);
    }

    @When("an event arrives requesting the merchants payment history")
    public void an_event_arrives_requesting_the_merchants_payment_history() {
        correlationId = UUID.randomUUID();

        Mockito.when(mockWaiter.synchronouslyWaitForReply(Mockito.any())).thenReturn(
                new Event(UserExistsReplied.topic, new Object[]{
                        new UserExistsReplied(
                                UUID.randomUUID(),
                                new UserExistsReplied.Success()
                        )
                }
                ));

        paymentService.handlePaymentMerchantHistoryRequest(
                new Event(PaymentHistoryRequested.PaymentMerchantHistoryRequested.topic, new Object[]{
                        new PaymentHistoryRequested.PaymentMerchantHistoryRequested(correlationId, merchantId)
                })
        );
    }

    @Then("the merchant payment history is fetched from the payment database and an event is published")
    public void the_merchant_payment_history_is_fetched_from_the_payment_database_and_an_event_is_published() {
        Assert.assertNotNull(merchantPaymentHistory);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(mockMessageQueue, Mockito.times(2)).publish(captor.capture());
        Event actual = captor.getAllValues().get(1);

        var reply = actual.getArgument(0, PaymentHistoryReplied.PaymentMerchantHistoryReplied.class);
        Assert.assertEquals(correlationId, reply.getCorrelationId());

        boolean equalItems = true;
        var actualHistory = reply.getSuccessResponse().getMerchantHistoryList();
        for (PaymentHistoryMerchant i : merchantPaymentHistory) {
            boolean containsEquivalent = false;
            for (PaymentHistoryMerchant j : actualHistory) {
                if (matchesMerchantHistory(i, j)) {
                    containsEquivalent = true;
                    break;
                }
            }
            if (!containsEquivalent) {
                equalItems = false;
                break;
            }
        }
        Assert.assertTrue(equalItems);
    }

    @Given("a payment exists in the payment database")
    public void a_payment_exists_in_the_payment_database() {
        UUID customerId = UUID.randomUUID();
        UUID merchantId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(100);
        String description = "Receipt: Eggs";
        Timestamp timestamp = Timestamp.valueOf("2029-01-20 08:59:59.0");
        String merchantName = "Fat Albert";
        String customerName = "John Johnson";
        Payment payment = new Payment(customerId, merchantId, amount, description, timestamp, customerName, merchantName);
        managerPaymentHistory.add(new PaymentHistoryManager(UUID.randomUUID(), amount, description, timestamp, merchantName, customerId, merchantId, customerName));
        paymentService.getPaymentHistory().addPaymentHistory(UUID.randomUUID(), payment);
    }

    @When("an event arrives requesting the managers payment history")
    public void an_event_arrives_requesting_the_managers_payment_history() {
        correlationId = UUID.randomUUID();

        Mockito.when(mockWaiter.synchronouslyWaitForReply(Mockito.any())).thenReturn(
                new Event(UserExistsReplied.topic, new Object[]{
                        new UserExistsReplied(
                                UUID.randomUUID(),
                                new UserExistsReplied.Success()
                        )
                }
                ));

        paymentService.handlePaymentManagerHistoryRequest(
                new Event(PaymentHistoryRequested.PaymentManagerHistoryRequested.topic, new Object[]{
                        new PaymentHistoryRequested.PaymentManagerHistoryRequested(correlationId)
                })
        );
    }

    @Then("the manager payment history is fetched from the payment database and an event is published")
    public void the_manager_payment_history_is_fetched_from_the_payment_database_and_an_event_is_published() {
        Assert.assertNotNull(managerPaymentHistory);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(mockMessageQueue).publish(captor.capture());
        Event actual = captor.getValue();

        var reply = actual.getArgument(0, PaymentHistoryReplied.PaymentManagerHistoryReplied.class);
        Assert.assertEquals(correlationId, reply.getCorrelationId());

        boolean equalItems = true;
        var actualHistory = reply.getSuccessResponse().getManagerHistoryList();
        for (PaymentHistoryManager i : managerPaymentHistory) {
            boolean containsEquivalent = false;
            for (PaymentHistoryManager j : actualHistory) {
                if (matchesManagerHistory(i, j)) {
                    containsEquivalent = true;
                    break;
                }
            }
            if (!containsEquivalent) {
                equalItems = false;
                break;
            }
        }
        Assert.assertTrue(equalItems);
    }

    @Given("a non existing merchant")
    public void a_non_existing_merchant() {
        this.merchantId = UUID.randomUUID();
    }

    @Given("a invalid token")
    public void a_invalid_token() {
        this.tokenId = UUID.randomUUID();
    }

    @Given("a negative amount of {int} with description {string}")
    public void a_negative_amount_of_with_description(Integer negativeAmount, String description) {
        this.amount = BigDecimal.valueOf(negativeAmount);
        this.description = description;
    }

    @Given("a too big amount of {int} with description {string}")
    public void a_too_big_amount_of_with_description(Integer tooBigAmount, String description) {
        this.amount = BigDecimal.valueOf(tooBigAmount);
        this.description = description;
    }

    @When("an event arrives requesting payment which will fail due to a non existing merchant")
    public void an_event_arrives_requesting_payment_which_will_fail_due_to_a_non_existing_merchant() throws BankServiceException_Exception {
        Account account = Mockito.mock(Account.class);
        Mockito.when(mockBank.getAccount(Mockito.any())).thenReturn(account);
        Mockito.when(account.getBalance()).thenReturn(amount);
        Mockito.when(mockWaiter.synchronouslyWaitForReply(Mockito.any())).thenReturn(
                new Event(BankAccountIdFromMerchantIdReplied.topic, new Object[]{
                        new BankAccountIdFromMerchantIdReplied(
                                UUID.randomUUID(),
                                new BaseReplyEvent.SimpleFailure("User not found")
                        )
                }
                ), new Event(BankAccountIdFromCustomerIdReplied.topic, new Object[]{
                        new BankAccountIdFromCustomerIdReplied(
                                UUID.randomUUID(),
                                new BankAccountIdFromCustomerIdReplied.Success()
                        )
                }
                ));

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
    }

    @When("an event arrives requesting payment which will fail due to an invalid token")
    public void an_event_arrives_requesting_payment_which_will_fail_due_to_an_invalid_token() throws BankServiceException_Exception {
        Account account = Mockito.mock(Account.class);
        Mockito.when(mockBank.getAccount(Mockito.any())).thenReturn(account);
        Mockito.when(account.getBalance()).thenReturn(amount);
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
                                new BaseReplyEvent.SimpleFailure("Customer is not found")
                        )
                }
                ));

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
    }

    @When("an event arrives requesting payment which will fail due to insufficient funds")
    public void an_event_arrives_requesting_payment_which_will_fail_due_to_insufficient_funds() throws BankServiceException_Exception {
        Account account = Mockito.mock(Account.class);
        Mockito.when(mockBank.getAccount(Mockito.any())).thenReturn(account);
        Mockito.when(account.getBalance()).thenReturn(amount.subtract(BigDecimal.ONE));
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
                ));

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
    }

    @When("an event arrives requesting payment which fails due to a bank exception")
    public void an_event_arrives_requesting_payment_which_fails_due_to_a_bank_exception() throws BankServiceException_Exception {
        Account account = Mockito.mock(Account.class);
        Mockito.when(mockBank.getAccount(Mockito.any())).thenReturn(account);
        Mockito.when(account.getBalance()).thenReturn(amount);
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
                ));

        Mockito.doThrow(BankServiceException_Exception.class)
                .when(mockBank)
                .transferMoneyFromTo(
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any());

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
    }

    @Then("a payment is not registered and an error event with the string {string} is published")
    public void a_payment_is_not_registered_and_an_error_event_with_the_string_is_published(String failureMessage) {
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(mockMessageQueue, Mockito.times(3)).publish(captor.capture());
        List<Event> actualAll = captor.getAllValues();
        this.payReplied = actualAll.get(2).getArgument(0, PayReplied.class);

        Assert.assertEquals(failureMessage, payReplied.getFailureResponse().getReason());
    }

    @Given("a non existing customer")
    public void a_non_existing_customer() {
        this.customerId = UUID.randomUUID();
    }

    @When("an event arrives requesting the merchants payment history which will fail due to cant find user")
    public void an_event_arrives_requesting_the_merchants_payment_history_which_will_fail_due_to_cant_find_user() {
        correlationId = UUID.randomUUID();

        Mockito.when(mockWaiter.synchronouslyWaitForReply(Mockito.any())).thenReturn(
                new Event(UserExistsReplied.topic, new Object[]{
                        new UserExistsReplied(
                                UUID.randomUUID(),
                                new BaseReplyEvent.SimpleFailure("User does not exist")
                        )
                }
                ));

        paymentService.handlePaymentMerchantHistoryRequest(
                new Event(PaymentHistoryRequested.PaymentMerchantHistoryRequested.topic, new Object[]{
                        new PaymentHistoryRequested.PaymentMerchantHistoryRequested(correlationId, merchantId)
                })
        );
    }

    @When("an event arrives requesting the customers payment history which will fail due to cant find user")
    public void an_event_arrives_requesting_the_customers_payment_history_which_will_fail_due_to_cant_find_user() {
        correlationId = UUID.randomUUID();

        Mockito.when(mockWaiter.synchronouslyWaitForReply(Mockito.any())).thenReturn(
                new Event(UserExistsReplied.topic, new Object[]{
                        new UserExistsReplied(
                                UUID.randomUUID(),
                                new BaseReplyEvent.SimpleFailure("User does not exist")
                        )
                }
                ));

        paymentService.handlePaymentCustomerHistoryRequest(
                new Event(PaymentHistoryRequested.PaymentCustomerHistoryRequested.topic, new Object[]{
                        new PaymentHistoryRequested.PaymentCustomerHistoryRequested(correlationId, customerId)
                })
        );
    }

    @Then("an error event for the customer with the message {string} is published")
    public void an_error_event_for_the_customer_with_the_message_is_published(String failureMessage) {
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(mockMessageQueue, Mockito.times(2)).publish(captor.capture());
        List<Event> actualAll = captor.getAllValues();
        var response = actualAll.get(1).getArgument(0, PaymentHistoryReplied.PaymentCustomerHistoryReplied.class);

        Assert.assertEquals(failureMessage, response.getFailureResponse().getReason());
    }

    @Then("an error event for the merchant with the message {string} is published")
    public void an_error_event_for_the_merchant_with_the_message_is_published(String failureMessage) {
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(mockMessageQueue, Mockito.times(2)).publish(captor.capture());
        List<Event> actualAll = captor.getAllValues();
        var response = actualAll.get(1).getArgument(0, PaymentHistoryReplied.PaymentMerchantHistoryReplied.class);

        Assert.assertEquals(failureMessage, response.getFailureResponse().getReason());
    }

    @Then("an error event for the manager with the message {string} is published")
    public void an_error_event_for_the_manager_with_the_message_is_published(String failureMessage) {
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(mockMessageQueue).publish(captor.capture());
        Event actual = captor.getValue();
        var response = actual.getArgument(0, PaymentHistoryReplied.PaymentManagerHistoryReplied.class);

        Assert.assertEquals(failureMessage, response.getFailureResponse().getReason());
    }

    @Given("a manager")
    public void a_manager() {
        // Do nothing
    }

}
