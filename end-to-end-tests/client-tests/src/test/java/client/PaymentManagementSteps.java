package client;

import event.payment.pay.PayReplied;
import event.token.TokensReplied;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;
import generated.dtu.ws.fastmoney.User;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import rest.PaymentHistoryCustomer;
import rest.PaymentHistoryManager;
import rest.PaymentHistoryMerchant;
import services.Manager.AccountsManagerClient;
import services.Manager.PaymentManagerClient;
import services.Manager.TokenManagerClient;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class PaymentManagementSteps {

    private final BankService bank = new BankServiceService().getBankServicePort();

    private final String customerCPR = "120789-1233";
    private final String merchantCPR = "240698-4623";
    private final String customerFirstname = "Ketr";
    private final String customerLastname = "Kubes";
    private final String merchantFirstname = "Naja";
    private final String merchantLastname = "Tubes";
    private Response customerPaymentResponse;
    private Response managerPaymentResponse;
    private Response merchantPaymentResponse;
    private String bankAccountCustomerId;
    private String bankAccountMerchantId;
    private UUID merchantId;
    private UUID customerId;
    private BigDecimal amount;
    private String description;
    private UUID tokenId;

    private Response paymentResponse;
    // @author : David
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

            new AccountsManagerClient().retireUser(customerCPR);
            new AccountsManagerClient().retireUser(merchantCPR);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // @author : David
    @Given("a customer with a bank account with balance {int}")
    public void a_customer_with_a_bank_account_with_balance(Integer customerBalance) throws BankServiceException_Exception {
        User user = new User();
        user.setCprNumber(customerCPR);
        user.setFirstName(customerFirstname);
        user.setLastName(customerLastname);
        bankAccountCustomerId = bank.createAccountWithBalance(user, BigDecimal.valueOf(customerBalance));
        var response = new AccountsManagerClient().registerUser(bankAccountCustomerId,
                user.getCprNumber(),
                user.getFirstName() + " " + user.getLastName(),
                false);
        customerId = response.readEntity(UUID.class);

        Response result = new TokenManagerClient().requestTokens(customerId, 1);
        List<UUID> tokens = result.readEntity(TokensReplied.Success.class).getTokens();

        tokenId = tokens.get(0);


    }
    // @author : Søren
    @Given("a merchant with a bank account with balance {int}")
    public void a_merchant_with_a_bank_account_with_balance(Integer merchantBalance) throws BankServiceException_Exception {
        User user = new User();
        user.setCprNumber(merchantCPR);
        user.setFirstName(merchantFirstname);
        user.setLastName(merchantLastname);
        bankAccountMerchantId = bank.createAccountWithBalance(user, BigDecimal.valueOf(merchantBalance));
        var response = new AccountsManagerClient().registerUser(bankAccountMerchantId,
                user.getCprNumber(),
                user.getFirstName() + " " + user.getLastName(),
                true);
        merchantId = response.readEntity(UUID.class);
    }
    // @author : Søren
    @Given("a merchant that does not exist")
    public void a_merchant_that_does_not_exist() {
        merchantId = UUID.randomUUID();
    }
    // @author : David
    @Given("the merchant asks the customer for payment of {int} kr and description {string}")
    public void theMerchantAsksTheCustomerForPaymentOfKrAndDescription(int amount, String description) {
        this.amount = BigDecimal.valueOf(amount);
        this.description = description;
    }
    // @author : Søren
    @Given("the customer gives the merchant an invalid tokenId through NFC")
    public void theCustomerGivesTheMerchantAnInvalidTokenIdThroughNFC() {
        tokenId = UUID.randomUUID();
    }
    // @author : Søren
    @When("the merchant requests the payment to DTUPay")
    public void the_merchant_requests_the_payment_to_dtu_pay() {
        paymentResponse = new PaymentManagerClient().pay(
                tokenId,
                merchantId,
                amount,
                description
        );
    }
    // @author : David
    @Then("the payment is successful")
    public void the_payment_is_successful() {
        assertEquals(200, paymentResponse.getStatus());
    }
    // @author : David
    @Then("the payment is unsuccessful")
    public void the_payment_is_unsuccessful() {
        assertEquals(400, paymentResponse.getStatus());
    }
    // @author : David
    @Then("an error message is returned saying {string} payment")
    public void an_error_message_is_returned_payment(String message) {
        assertEquals(message, paymentResponse.readEntity(PayReplied.PayRepliedFailure.class).getReason());
    }
    // @author : Søren
    @Then("the balance of the customer at the bank is {int} kr")
    public void the_balance_of_the_customer_at_the_bank_is_kr(Integer balance) throws BankServiceException_Exception {
        assertEquals(
                0,
                BigDecimal.valueOf(balance).compareTo(bank.getAccount(bankAccountCustomerId).getBalance())
        );
    }
    // @author : Søren
    @Then("the balance of the merchant at the bank is {int} kr")
    public void the_balance_of_the_merchant_at_the_bank_is_kr(Integer balance) throws BankServiceException_Exception {
        assertEquals(
                0,
                BigDecimal.valueOf(balance).compareTo(bank.getAccount(bankAccountMerchantId).getBalance())
        );
    }
    // @author : Søren
    @When("the customer requests his payments")
    public void the_customer_requests_his_payments() {
        customerPaymentResponse = new PaymentManagerClient().customerPaymentHistory(customerId);
    }
    // @author : David
    @Then("the customer receives their payments")
    public void the_customer_receives_their_payments() {
        assertEquals(200, customerPaymentResponse.getStatus());
        List<PaymentHistoryCustomer> customerPaymentList = customerPaymentResponse.readEntity(new GenericType<>() {
        });
        assertEquals(
                0,
                amount.compareTo(customerPaymentList.get(0).getAmount())
        );

        assertEquals(
                description,
                customerPaymentList.get(0).getDescription()
        );
        System.out.println("Payment summary for " + customerFirstname + " " + customerLastname);
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        customerPaymentList.forEach(p -> System.out.println(
                "Payment ID: " + p.getPaymentId() + "\n" +
                        "Merchant: " + p.getMerchantName() + "\n" +
                        "Amount: " + p.getAmount() + "\n" +
                        "Description: " + p.getDescription() + "\n" +
                        "Time: " + p.getTimestamp() + "\n" +
                        "----------------------------------------"
        ));

    }
    // @author : David
    @Given("the manager")
    public void theManager() {
        // Do nothing
    }

    // @author : Søren
    @When("the merchant requests his payments")
    public void the_merchant_requests_his_payments() {
        merchantPaymentResponse = new PaymentManagerClient().merchantPaymentHistory(merchantId);
    }
    // @author : David
    @Then("the merchant receives a list of all their payments")
    public void the_merchant_receives_a_list_of_all_their_payments() {
        assertEquals(200, merchantPaymentResponse.getStatus());
        List<PaymentHistoryMerchant> merchantPaymentList = merchantPaymentResponse.readEntity(new GenericType<>() {
        });
        assertEquals(
                0,
                amount.compareTo(merchantPaymentList.get(0).getAmount())
        );

        assertEquals(
                description,
                merchantPaymentList.get(0).getDescription()
        );
        System.out.println("Payment summary for " + merchantFirstname + " " + merchantLastname);
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        merchantPaymentList.forEach(p -> System.out.println(
                "Payment ID: " + p.getPaymentId() + "\n" +
                        "Amount: " + p.getAmount() + "\n" +
                        "Description: " + p.getDescription() + "\n" +
                        "Time: " + p.getTimestamp() + "\n" +
                        "----------------------------------------"
        ));
    }
    // @author : Søren
    @When("the manager requests all payments")
    public void the_manager_requests_all_payments() {
        managerPaymentResponse = new PaymentManagerClient().managerPaymentHistory();
    }
    // @author : Søren
    @Then("the manager receives a list of all payments")
    public void the_manager_receives_a_list_of_all_payments() {
        assertEquals(200, managerPaymentResponse.getStatus());
        List<PaymentHistoryManager> managerPaymentList = managerPaymentResponse.readEntity(new GenericType<>() {
        });
        System.out.println("Full payment summary");
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        managerPaymentList.forEach(p -> System.out.println(
                "Payment ID: " + p.getPaymentId() + "\n" +
                        "Customer: " + p.getCustomerId() + " (" + p.getCustomerName() + ")\n" +
                        "Merchant: " + p.getMerchantId() + " (" + p.getMerchantName() + ")\n" +
                        "Amount: " + p.getAmount() + "\n" +
                        "Description: " + p.getDescription() + "\n" +
                        "Time: " + p.getTimestamp() + "\n" +
                        "----------------------------------------"
        ));
    }
    // @author : David
    @Given("a customer that does not exist")
    public void a_customer_that_does_not_exist() {
        this.customerId = UUID.randomUUID();
    }
    // @author : David
    @Then("an error message is returned saying {string} customer history")
    public void an_error_message_is_returned_saying_customer_history(String failureMessage) {
        assertEquals(400, customerPaymentResponse.getStatus());
        assertEquals(failureMessage, customerPaymentResponse.readEntity(String.class));
    }
    // @author : David
    @Then("an error message is returned saying {string} merchant history")
    public void an_error_message_is_returned_saying_merchant_history(String failureMessage) {
        assertEquals(400, merchantPaymentResponse.getStatus());
        assertEquals(failureMessage, merchantPaymentResponse.readEntity(String.class));
    }
    // @author : David
    @Then("an error message is returned saying {string} manager history")
    public void an_error_message_is_returned_saying_manager_history(String failureMessage) {
        assertEquals(400, managerPaymentResponse.getStatus());
        assertEquals(failureMessage, managerPaymentResponse.readEntity(String.class));
    }

}
