package client.paymentmanagementsteps;

import dk.dtu.team14.PaymentService;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;
import generated.dtu.ws.fastmoney.User;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javax.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class SuccessfulPayment {

    private final BankService bank = new BankServiceService().getBankServicePort();
    private final String customerCPR = "111111-1111";
    private final String merchantCPR = "222222-2222";
    private String bankAccountCustomerId;
    private String bankAccountMerchantId;
    private BigDecimal amount;
    private String description;
    private String tokenId = UUID.randomUUID().toString();

    private Payment payment;

    @Before
    public void deleteAccounts() {
        bank.getAccounts()
                .stream()
                .filter(accountInfo ->
                        accountInfo.getUser().getCprNumber().equals(customerCPR) ||
                        accountInfo.getUser().getCprNumber().equals(merchantCPR)
                ).forEach(accountInfo -> {
                    try {
                        bank.retireAccount(accountInfo.getAccountId());
                    } catch (BankServiceException_Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Given("a customer with a bank account with balance {int}")
    public void a_customer_with_a_bank_account_with_balance(Integer customerBalance) throws BankServiceException_Exception {
        User user = new User();
        user.setCprNumber(customerCPR);
        user.setFirstName("Customer");
        user.setLastName("One");
        bankAccountCustomerId = bank.createAccountWithBalance(user, BigDecimal.valueOf(customerBalance));
    }
    @Given("a merchant with a bank account with balance {int}")
    public void a_merchant_with_a_bank_account_with_balance(Integer merchantBalance) throws BankServiceException_Exception {
        User user = new User();
        user.setCprNumber(merchantCPR);
        user.setFirstName("Merchant");
        user.setLastName("One");
        bankAccountMerchantId = bank.createAccountWithBalance(user, BigDecimal.valueOf(merchantBalance));
    }
    @When("the merchant initiates a payment for {int} kr and description {string}")
    public void the_merchant_initiates_a_payment_for_kr_and_description(Integer amount, String description) {
        this.amount = BigDecimal.valueOf(amount);
        this.description = description;
    }
    @When("the customer gives the merchant their {string}")
    public void the_customer_gives_the_merchant_their(String tokenId) {
        //this.tokenId = tokenId;//TODO
    }
    
    @Then("the merchant requests the payment to DTUPay")
    public void the_merchant_requests_the_payment_to_dtu_pay() {
        Response response = new PaymentService().pay(tokenId, bankAccountCustomerId, bankAccountMerchantId, amount, description);
        assertEquals( 200, response.getStatus());
    }
    @When("the payment is successful")
    public void the_payment_is_successful() {
        //Do nothing
    }
    @Then("the balance of the customer at the bank is {int} kr")
    public void the_balance_of_the_customer_at_the_bank_is_kr(Integer balance) throws BankServiceException_Exception {
        assertEquals(BigDecimal.valueOf(balance).compareTo(bank.getAccount(bankAccountCustomerId).getBalance()), 0);
    }
    @Then("the balance of the merchant at the bank is {int} kr")
    public void the_balance_of_the_merchant_at_the_bank_is_kr(Integer balance) throws BankServiceException_Exception {
        assertEquals(BigDecimal.valueOf(balance).compareTo(bank.getAccount(bankAccountMerchantId).getBalance()), 0);
    }
}
