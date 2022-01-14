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
import rest.Payment;

import java.math.BigDecimal;
import java.util.List;

public class SuccessfulHistoryRetrievalByCustomer {

    private String baseUrl = "http://localhost:8080";

    private final BankService bank = new BankServiceService().getBankServicePort();

    private List<Payment> paymentList;
    private String bankAccountCustomerId;

    private final String customerCPR = "333333-1112";

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

    @Given("a customer")
    public void a_customer() throws BankServiceException_Exception {
        User user = new User();
        user.setCprNumber(customerCPR);
        user.setFirstName("Customer");
        user.setLastName("Two");
        bankAccountCustomerId = bank.createAccountWithBalance(user, BigDecimal.valueOf(100));

    }
    @When("the customer requests his payments")
    public void the_customer_requests_his_payments() {
        paymentList = new PaymentService(baseUrl).getPaymentsForUser(bankAccountCustomerId);
    }
    @Then("the customer receives a list of all their payments")
    public void the_customer_receives_a_list_of_all_their_payments() {
        paymentList.forEach(p -> System.out.println(p.debtorId));
    }

}
