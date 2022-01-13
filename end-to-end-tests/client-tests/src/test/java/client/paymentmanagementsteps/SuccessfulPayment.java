package client.paymentmanagementsteps;

import client.services.PaymentService;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;
import generated.dtu.ws.fastmoney.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import rest.Payment;

import javax.ws.rs.core.Response;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class SuccessfulPayment {

    private String baseUrl = "http://localhost:8080";

    private final BankService bank = new BankServiceService().getBankServicePort();
    private String bankAccountCustomerId;
    private String bankAccountMerchantId;

    private String customerId;
    private String merchantId;
    private int customerBalance;
    private int merchantBalance;
    private double amount;
    private String description;

    private String tokenId;

    private Payment payment;


    @Given("a customer with id {string} with a bank account with balance {int}")
    public void a_customer_with_id_with_a_bank_account_with_balance(String customerId, Integer customerBalance) throws BankServiceException_Exception {
        this.customerId = customerId;
        this.customerBalance = customerBalance;
        User user = new User();
        user.setCprNumber("111111-1111");
        user.setFirstName("Customer");
        user.setLastName("One");
        bankAccountCustomerId = bank.createAccountWithBalance(user, BigDecimal.valueOf(customerBalance));
    }
    @Given("a merchant with id {string} with a bank account with balance {int}")
    public void a_merchant_with_id_with_a_bank_account_with_balance(String merchantId, Integer merchantBalance) throws BankServiceException_Exception {
        this.merchantId = merchantId;
        this.merchantBalance = merchantBalance;
        User user = new User();
        user.setCprNumber("222222-2222");
        user.setFirstName("Merchant");
        user.setLastName("One");
        bankAccountMerchantId = bank.createAccountWithBalance(user, BigDecimal.valueOf(merchantBalance));
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
    public void the_balance_of_the_customer_at_the_bank_is_kr(Integer balance) throws BankServiceException_Exception {
        assertEquals(BigDecimal.valueOf(balance), bank.getAccount(bankAccountCustomerId).getBalance());
    }
    @Then("the balance of the merchant at the bank is {int} kr")
    public void the_balance_of_the_merchant_at_the_bank_is_kr(Integer balance) throws BankServiceException_Exception {
        assertEquals(BigDecimal.valueOf(balance), bank.getAccount(bankAccountMerchantId).getBalance());
    }
}
