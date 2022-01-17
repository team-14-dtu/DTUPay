package client.paymentmanagementsteps;

import dk.dtu.team14.PaymentService;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import rest.User;

import java.math.BigDecimal;
import java.util.List;

public class SuccessfulHistoryRetrievalByCustomer {

//    private final BankService bank = new BankServiceService().getBankServicePort();
//    private List<Payment> paymentList;
//    private final String customerCPR = "444444-1112";
//
//    @Before
//    public void deleteAccounts() {
//        bank.getAccounts()
//                .stream()
//                .filter(accountInfo ->
//                        accountInfo.getUser().getCprNumber().equals(customerCPR)
//                ).forEach(accountInfo -> {
//                    try {
//                        bank.retireAccount(accountInfo.getAccountId());
//                    } catch (BankServiceException_Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//    }
//
//    @Given("a customer")
//    public void a_customer() throws BankServiceException_Exception {
//        generated.dtu.ws.fastmoney.User user = new generated.dtu.ws.fastmoney.User();
//        user.setCprNumber(customerCPR);
//        user.setFirstName("Merchant");
//        user.setLastName("Two");
//        String bankAccountCustomerId = bank.createAccountWithBalance(user, BigDecimal.valueOf(100));
//    }
//
//    @When("the customer requests his payments")
//    public void the_customer_requests_his_payments() {
//        paymentList = new PaymentService().paymentHistory("customerId1", User.Type.CUSTOMER); //TODO creates different steps for customer/merchant/manager
//    }
//    @Then("the customer receives a list of all their payments")
//    public void the_customer_receives_a_list_of_all_their_payments() {
//        paymentList.forEach(p -> System.out.println(p.getAmount().multiply(BigDecimal.valueOf(-1))));
//    }

}
