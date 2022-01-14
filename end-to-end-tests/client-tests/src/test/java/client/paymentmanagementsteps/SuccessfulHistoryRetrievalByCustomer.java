package client.paymentmanagementsteps;

import dk.dtu.team14.PaymentService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import rest.Payment;

import java.util.List;

public class SuccessfulHistoryRetrievalByCustomer {

    private String baseUrl = "http://localhost:8080";

    private List<Payment> paymentList;
    private String customerId;

    @Given("a payment customer with id {string}")
    public void a_payment_customer_with_id(String customerId) {
        this.customerId = customerId;
    }
    @When("the user requests his payments")
    public void the_user_requests_his_payments() {
        paymentList = new PaymentService(baseUrl).getPaymentsForUser(customerId);
    }
    @Then("the user receives a list of all their payments")
    public void the_user_receives_a_list_of_all_their_payments() {
        paymentList.forEach(p -> System.out.println(p.debtorId));
    }

}
