package client.paymentmanagementsteps;

import services.PaymentClient;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Before;
import rest.PaymentHistoryCustomer;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public class SuccessfulHistoryRetrievalByCustomer {

    private final BankService bank = new BankServiceService().getBankServicePort();
    private List<PaymentHistoryCustomer> paymentList;
    private final String customerCPR = "444444-1112";
    private final String customerFirstname = "Keanu";
    private final String customerLastname = "Reeves";
    private UUID uuidCustomer;

    @Before
    public void deleteAccounts() {
        bank.getAccounts()
                .stream()
                .filter(accountInfo ->
                        accountInfo.getUser().getCprNumber().equals(customerCPR)
                ).forEach(accountInfo -> {
                    try {
                        bank.retireAccount(accountInfo.getAccountId());
                    } catch (BankServiceException_Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Given("a payment customer with id {string}")
    public void aPaymentCustomerWithId(String customerId) {
        uuidCustomer = new UUID(new BigInteger((customerId.replace("-", "")).substring(0, 16), 16).longValue(), new BigInteger((customerId.replace("-", "")).substring(16), 16).longValue());

    }

    @When("the customer requests his payments")
    public void the_customer_requests_his_payments() {
        paymentList = new PaymentClient().customerPaymentHistory(uuidCustomer);
    }

    @Then("the customer receives a list of all their payments")
    public void the_customer_receives_a_list_of_all_their_payments() {
        System.out.println("Payment summary for " + customerFirstname + " " + customerLastname);
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        paymentList.forEach(p -> System.out.println(
                "Payment ID: " + p.getPaymentId() + "\n" +
                "Merchant: " + p.getMerchantName() + "\n" +
                "Amount: " + p.getAmount() + "\n" +
                "Description: " + p.getDescription() + "\n" +
                "Time: " + p.getTimestamp() + "\n" +
                "----------------------------------------"
        ));
    }

}
