package client.paymentmanagementsteps;

import services.PaymentClient;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import rest.PaymentHistoryMerchant;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public class SuccessfulHistoryRetrievalByMerchant {

    private final BankService bank = new BankServiceService().getBankServicePort();
    private List<PaymentHistoryMerchant> paymentList;
    private final String merchantCPR = "444444-1113";
    private final String merchantFirstname = "Sammuel";
    private final String merchantLastname = "L. Jackson";
    private UUID uuidMerchant;

    @Before
    public void deleteAccounts() {
        bank.getAccounts()
                .stream()
                .filter(accountInfo ->
                        accountInfo.getUser().getCprNumber().equals(merchantCPR)
                ).forEach(accountInfo -> {
                    try {
                        bank.retireAccount(accountInfo.getAccountId());
                    } catch (BankServiceException_Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Given("a merchant with id {string}")
    public void aMerchantWithId(String merchantId) {
        uuidMerchant = new UUID(new BigInteger((merchantId.replace("-", "")).substring(0, 16), 16).longValue(), new BigInteger((merchantId.replace("-", "")).substring(16), 16).longValue());
    }

    @When("the merchant requests his payments")
    public void theMerchantRequestsHisPayments() {
        paymentList = new PaymentClient().merchantPaymentHistory(uuidMerchant);
    }

    @Then("the merchant receives a list of all their payments")
    public void theMerchantReceivesAListOfAllTheirPayments() {
        System.out.println("Payment summary for " + merchantFirstname + " " + merchantLastname);
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        paymentList.forEach(p -> System.out.println(
                "Payment ID: " + p.getPaymentId() + "\n" +
                        "Amount: " + p.getAmount() + "\n" +
                        "Description: " + p.getDescription() + "\n" +
                        "Time: " + p.getTimestamp() + "\n" +
                        "----------------------------------------"
        ));
    }
}
