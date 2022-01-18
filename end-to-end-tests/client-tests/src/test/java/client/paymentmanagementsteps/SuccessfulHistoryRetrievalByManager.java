package client.paymentmanagementsteps;

import dk.dtu.team14.PaymentClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import rest.PaymentHistoryManager;

import java.util.List;

public class SuccessfulHistoryRetrievalByManager {

    private List<PaymentHistoryManager> paymentList;

    @Given("the manager")
    public void theManager() {
        // Do nothing
    }
    @When("the manager requests all payments")
    public void theManagerRequestsAllPayments() {
        paymentList = new PaymentClient().managerPaymentHistory();
    }
    @Then("the manager receives a list of all payments")
    public void theManagerReceivesAListOfAllPayments() {
        paymentList.forEach(p -> System.out.println(p));
    }

}
