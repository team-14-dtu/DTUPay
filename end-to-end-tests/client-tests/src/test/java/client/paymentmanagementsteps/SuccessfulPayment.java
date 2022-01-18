package client.paymentmanagementsteps;

import dk.dtu.team14.AccountsClient;
import dk.dtu.team14.CustomerClient;
import dk.dtu.team14.PaymentClient;
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
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class SuccessfulPayment {

    private final BankService bank = new BankServiceService().getBankServicePort();

    private final String customerCPR = "120789-1233";
    private final String merchantCPR = "240698-4623";
    private String bankAccountCustomerId;
    private String bankAccountMerchantId;
    private UUID merchantId;
    private UUID customerId;
    private BigDecimal amount;
    private String description;
    private UUID tokenId;

    private Response paymentResponse;

    @Before
    public void deleteAccounts() {
        try {
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

            new AccountsClient().retireUser(customerCPR);
            new AccountsClient().retireUser(merchantCPR);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    @Given("a customer with a bank account with balance {int}")
    public void a_customer_with_a_bank_account_with_balance(Integer customerBalance) throws BankServiceException_Exception {
        //makeAccountsOnce();
        User user = new User();
        user.setCprNumber(customerCPR);
        user.setFirstName("Ketr");
        user.setLastName("Kubes");
        //bankAccountCustomerId = "ee571faa-d11e-4111-b68c-96c5179b843f";
        bankAccountCustomerId = bank.createAccountWithBalance(user, BigDecimal.valueOf(customerBalance));
        var response = new AccountsClient().registerUser(bankAccountCustomerId,
                user.getCprNumber(),
                user.getFirstName()+" "+user.getLastName(),
                false);
        customerId = response.readEntity(UUID.class);

        List<UUID> tokens = new CustomerClient().requestTokens(customerId,1);

        tokenId = tokens.get(0);


    }

    @Given("a merchant with a bank account with balance {int}")
    public void a_merchant_with_a_bank_account_with_balance(Integer merchantBalance) throws BankServiceException_Exception {
        User user = new User();
        user.setCprNumber(merchantCPR);
        user.setFirstName("Naja");
        user.setLastName("Tubes");
        bankAccountMerchantId = bank.createAccountWithBalance(user, BigDecimal.valueOf(merchantBalance));
        var response = new AccountsClient().registerUser(bankAccountMerchantId,
                user.getCprNumber(),
                user.getFirstName()+" "+user.getLastName(),
                true);
        merchantId = response.readEntity(UUID.class);
    }

    @Given("the merchant asks the customer for payment of {int} kr and description {string}")
    public void theMerchantAsksTheCustomerForPaymentOfKrAndDescription(int amount, String description) {
        this.amount = BigDecimal.valueOf(amount);
        this.description = description;
    }

    @Given("the customer gives the merchant their tokenId through NFC {string}")
    public void theCustomerGivesTheMerchantTheirTokenIdThroughNFC(String tokenId) {

    }
    
    @When("the merchant requests the payment to DTUPay")
    public void the_merchant_requests_the_payment_to_dtu_pay() {
        System.out.println("customer bank: " + bankAccountCustomerId );
        System.out.println("merchant bank: " + bankAccountMerchantId );
        paymentResponse = new PaymentClient().pay(
                tokenId,
                merchantId,
                amount,
                description
        );
    }
    @Then("the payment is successful")
    public void the_payment_is_successful() {
        assertEquals(200, paymentResponse.getStatus());
    }
    @Then("the balance of the customer at the bank is {int} kr")
    public void the_balance_of_the_customer_at_the_bank_is_kr(Integer balance) throws BankServiceException_Exception {
        assertEquals(
                0,
                BigDecimal.valueOf(balance).compareTo(bank.getAccount(bankAccountCustomerId).getBalance())
        );
    }
    @Then("the balance of the merchant at the bank is {int} kr")
    public void the_balance_of_the_merchant_at_the_bank_is_kr(Integer balance) throws BankServiceException_Exception {

        assertEquals(
                0,
                BigDecimal.valueOf(balance).compareTo(bank.getAccount(bankAccountMerchantId).getBalance())
        );
    }
}
