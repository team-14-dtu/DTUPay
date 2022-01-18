package client.paymentmanagementsteps;

import dk.dtu.team14.App;
import dk.dtu.team14.CustomerService;
import dk.dtu.team14.PaymentService;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;
import generated.dtu.ws.fastmoney.User;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
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
    private String merchantId;
    private String customerId;
    private BigDecimal amount;
    private String description;
    private String tokenId;

    private Response paymentResponse;

    /*public void makeAccountsOnce() throws BankServiceException_Exception {
        User customer1 = new User();
        customer1.setCprNumber("435465-0000");
        customer1.setFirstName("Customer");
        customer1.setLastName("One");
        String bankAccountCustomerId1 = bank.createAccountWithBalance(customer1, BigDecimal.valueOf(1000));
        User merchant1 = new User();
        merchant1.setCprNumber("987654-1119");
        merchant1.setFirstName("Merchant");
        merchant1.setLastName("One");
        String bankAccountMerchantId1 = bank.createAccountWithBalance(merchant1, BigDecimal.valueOf(1000));
        System.out.println("Customer Id "+bankAccountCustomerId1+ "\nMerchant Id "+bankAccountMerchantId1);
        // Customer ID : ee571faa-d11e-4111-b68c-96c5179b843f
        // Merchant ID : 4cc27026-6a38-41d3-8527-2a743caeedaf
    }*/

    /*@Before
    public void resetBalance() {
        try {
            bank.transferMoneyFromTo("4cc27026-6a38-41d3-8527-2a743caeedaf","ee571faa-d11e-4111-b68c-96c5179b843f",BigDecimal.valueOf(100),"resetting balance");
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }
    }*/

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

            new App().retireUser(customerCPR);
            new App().retireUser(merchantCPR);
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
        customerId = new App().registerUser(bankAccountCustomerId,
                user.getCprNumber(),
                user.getFirstName()+" "+user.getLastName(),
                false);

        List<UUID> tokens = new CustomerService().requestTokens(UUID.fromString(customerId),1);

        tokenId = tokens.get(0).toString();


    }

    @Given("a merchant with a bank account with balance {int}")
    public void a_merchant_with_a_bank_account_with_balance(Integer merchantBalance) throws BankServiceException_Exception {
//        this.merchantId = "c64e0015-fc28-4e0c-a5db-24d972117706";
        User user = new User();
        user.setCprNumber(merchantCPR);
        user.setFirstName("Naja");
        user.setLastName("Tubes");
        bankAccountMerchantId = bank.createAccountWithBalance(user, BigDecimal.valueOf(merchantBalance));
//        bankAccountMerchantId = "4cc27026-6a38-41d3-8527-2a743caeedaf";
        merchantId = new App().registerUser(bankAccountMerchantId,
                user.getCprNumber(),
                user.getFirstName()+" "+user.getLastName(),
                true);
    }

    @Given("the merchant asks the customer for payment of {int} kr and description {string}")
    public void theMerchantAsksTheCustomerForPaymentOfKrAndDescription(int amount, String description) {
        this.amount = BigDecimal.valueOf(amount);
        this.description = description;
    }

    @Given("the customer gives the merchant their tokenId through NFC {string}")
    public void theCustomerGivesTheMerchantTheirTokenIdThroughNFC(String tokenId) {
        //this.tokenId = tokenId;
    }
    
    @When("the merchant requests the payment to DTUPay")
    public void the_merchant_requests_the_payment_to_dtu_pay() {
        System.out.println("customer bank: " + bankAccountCustomerId );
        System.out.println("merchant bank: " + bankAccountMerchantId );
        paymentResponse = new PaymentService().pay(
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
