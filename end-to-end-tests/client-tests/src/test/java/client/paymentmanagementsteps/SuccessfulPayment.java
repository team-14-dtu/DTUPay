package client.paymentmanagementsteps;

import client.services.PaymentService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import rest.Payment;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class SuccessfulPayment {

    private String baseUrl = "http://localhost:8080";

    private String customerId;
    private String merchantId;
    private int customerBalance;
    private int merchantBalance;
    private double amount;
    private String description;

    private String tokenId;

    private Response response;
    private Payment payment;


    @Given("a customer with id {string} with a bank account with balance {int}")
    public void a_customer_with_id_with_a_bank_account_with_balance(String customerId, Integer customerBalance) {
        this.customerId = customerId;
        this.customerBalance = customerBalance;
    }
    @Given("a merchant with id {string} with a bank account with balance {int}")
    public void a_merchant_with_id_with_a_bank_account_with_balance(String merchantId, Integer merchantBalance) {
        this.merchantId = merchantId;
        this.merchantBalance = merchantBalance;
    }
    @When("the merchant initiates a payment for {double} kr and description {string}")
    public void the_merchant_initiates_a_payment_for_kr_and_description(double amount, String description) {
        this.amount = amount;
        this.description = description;
    }
    @When("the customer gives the merchant their {string}")
    public void the_customer_gives_the_merchant_their(String tokenId) {
        this.tokenId = tokenId;
    }
    @Then("the merchant requests the payment to DTUPay")
    public void the_merchant_requests_the_payment_to_dtu_pay() {
        Response response = new PaymentService(baseUrl).pay(tokenId, merchantId, amount, description);
        assertEquals( 200, response.getStatus());
    }
    @When("the payment is successful")
    public void the_payment_is_successful() {
        this.payment = new PaymentService(baseUrl).getTargetPayment(tokenId); //Notice tokenId = paymentId
        assertEquals(this.customerId, payment.getDebtorId());
        assertEquals(this.merchantId, payment.getCreditorId());
        assertEquals((int) Math.round(this.amount), (int) Math.round(payment.getAmount()));
        assertEquals(this.description, payment.getDescription());
    }
    @Then("the balance of the customer at the bank is {int} kr")
    public void the_balance_of_the_customer_at_the_bank_is_kr(Integer int1) {
        //TODO: This should call BankService and check the customerId balance
        throw new io.cucumber.java.PendingException();
    }
    @Then("the balance of the merchant at the bank is {int} kr")
    public void the_balance_of_the_merchant_at_the_bank_is_kr(Integer int1) {
        //TODO: This should call BankService and check the merchantId balance
        throw new io.cucumber.java.PendingException();
    }

}
