package client.paymentmanagementsteps;

import dk.dtu.team14.PaymentServiceFacade;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import rest.Payment;
import rest.User;

import java.util.List;

public class SuccessfulHistoryRetrievalByManager {

    private String baseUrl = "http://localhost:8080";

    private List<Payment> paymentList;

    @Given("the manager")
    public void theManager() {
        // Do nothing
    }
    @When("the manager requests all payments")
    public void theManagerRequestsAllPayments() {
        paymentList = new PaymentServiceFacade(baseUrl).getPaymentsForUser("", User.Type.MANAGER);
    }
    @Then("the manager receives a list of all payments")
    public void theManagerReceivesAListOfAllPayments() {
        paymentList.forEach(p -> System.out.println(p));
    }

}
