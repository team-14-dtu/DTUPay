package client.paymentmanagementsteps;

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
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class SuccessfulPayment {

    private final BankService bank = new BankServiceService().getBankServicePort();
    private final String customerCPR = "111111-1111";
    private final String merchantCPR = "222222-2222";
    private String bankAccountCustomerId;
    private String bankAccountMerchantId;
    private String merchantId;
    private BigDecimal amount;
    private String description;
    private String tokenId;

    private Response paymentResponse;

    public void makeAccountsOnce() throws BankServiceException_Exception {
        User customer1 = new User();
        customer1.setCprNumber("435465-0000");
        customer1.setFirstName("Customer");
        customer1.setLastName("One");
        String bankAccountCustomerId1 = bank.createAccountWithBalance(customer1, BigDecimal.valueOf(1000));
        User merchant1 = new User();
        merchant1.setCprNumber("987654-1111");
        merchant1.setFirstName("Merchant");
        merchant1.setLastName("One");
        String bankAccountMerchantId1 = bank.createAccountWithBalance(merchant1, BigDecimal.valueOf(1000));
        System.out.println("Customer Id "+bankAccountCustomerId1+ "\nMerchant Id "+bankAccountMerchantId1);
        // Customer ID : aca9bfe3-f300-4e7a-bcf2-6e0365ea1eda
        // Merchant ID : de2985e3-7803-4aab-9c99-e778762a0786
    }

    @Before
    public void resetBalance() {
        try {
            bank.transferMoneyFromTo("de2985e3-7803-4aab-9c99-e778762a0786","aca9bfe3-f300-4e7a-bcf2-6e0365ea1eda",BigDecimal.valueOf(100),"resetting balance");
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }
    }

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
        //makeAccountsOnce();
//        User user = new User();
//        user.setCprNumber(customerCPR);
//        user.setFirstName("Customer");
//        user.setLastName("One");
        bankAccountCustomerId = "aca9bfe3-f300-4e7a-bcf2-6e0365ea1eda";
        //bankAccountCustomerId = bank.createAccountWithBalance(user, BigDecimal.valueOf(customerBalance));
    }

    @Given("a merchant with a bank account with balance {int}")
    public void a_merchant_with_a_bank_account_with_balance(Integer merchantBalance) throws BankServiceException_Exception {
//        this.merchantId = "c64e0015-fc28-4e0c-a5db-24d972117706";
//        User user = new User();
//        user.setCprNumber(merchantCPR);
//        user.setFirstName("Merchant");
//        user.setLastName("One");
//        bankAccountMerchantId = bank.createAccountWithBalance(user, BigDecimal.valueOf(merchantBalance));
        bankAccountMerchantId = "de2985e3-7803-4aab-9c99-e778762a0786";
    }

    @Given("the merchant asks the customer for payment of {int} kr and description {string}")
    public void theMerchantAsksTheCustomerForPaymentOfKrAndDescription(int amount, String description) {
        this.amount = BigDecimal.valueOf(amount);
        this.description = description;
    }

    @Given("the customer gives the merchant their tokenId through NFC {string}")
    public void theCustomerGivesTheMerchantTheirTokenIdThroughNFC(String tokenId) {
        this.tokenId = tokenId;
    }
    
    @When("the merchant requests the payment to DTUPay")
    public void the_merchant_requests_the_payment_to_dtu_pay() {
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
                //BigDecimal.valueOf(balance).compareTo(bank.getAccount(bankAccountCustomerId).getBalance())
                BigDecimal.valueOf(balance).compareTo(bank.getAccount("aca9bfe3-f300-4e7a-bcf2-6e0365ea1eda").getBalance())
        );
    }
    @Then("the balance of the merchant at the bank is {int} kr")
    public void the_balance_of_the_merchant_at_the_bank_is_kr(Integer balance) throws BankServiceException_Exception {

        assertEquals(
                0,
                //BigDecimal.valueOf(balance).compareTo(bank.getAccount(bankAccountMerchantId).getBalance())
                BigDecimal.valueOf(balance).compareTo(bank.getAccount("de2985e3-7803-4aab-9c99-e778762a0786").getBalance())
        );
    }
}
