package client.paymentmanagementsteps;

import dk.dtu.team14.PaymentService;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import rest.Payment;
import rest.User;

import java.math.BigDecimal;
import java.util.List;

public class SuccessfulHistoryRetrievalByMerchant {

    private final BankService bank = new BankServiceService().getBankServicePort();

    private List<Payment> paymentList;
    private String bankAccountMerchantId;

    private final String merchantCPR = "444444-1113";

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

    @Given("a merchant")
    public void a_merchant() throws BankServiceException_Exception {
        generated.dtu.ws.fastmoney.User bankUser = new generated.dtu.ws.fastmoney.User();
        bankUser.setCprNumber(merchantCPR);
        bankUser.setFirstName("Customer");
        bankUser.setLastName("Two");
        bankAccountMerchantId = bank.createAccountWithBalance(bankUser, BigDecimal.valueOf(100));

    }
    @When("the merchant requests his earnings")
    public void the_merchant_requests_his_earnings() {
        paymentList = new PaymentService().paymentHistory("merchantId1", User.Type.MERCHANT);
    }
    @Then("the merchant receives a list of all their earnings")
    public void the_merchant_receives_a_list_of_all_their_earnings() {
        paymentList.forEach(p -> System.out.println(p.getAmount()));
    }

}
