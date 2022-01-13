package client;

import client.paymentservice.PaymentService;
import dk.dtu.team14.App;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import rest.Payment;
import rest.User;

import java.util.List;

public class ExampleSteps {

    private static String baseUrl = "http://localhost:8080";

    private User response;
    private List<Payment> paymentList;

    String cid;

    @Given("nothing")
    public void nothing() {
    }

    @When("we go to the website")
    public void weGoToTheWebsite() {
        App app = new App(baseUrl);
        response = app.getResponse();
    }

    @Then("it does not show error")
    public void itDoesNotShowError() {
        Assert.assertEquals("Petr", response.getName());
    }

    @Given("a customer with id {string}")
    public void a_customer_with_id(String string) {
        // Write code here that turns the phrase above into concrete actions
        this.cid = string;
    }
    @When("the user requests his payments")
    public void the_user_requests_his_payments() {
        // Write code here that turns the phrase above into concrete actions
        paymentList = new PaymentService(baseUrl).getPaymentsForUser("cid");
    }
    @Then("the user receives a list of all their payments")
    public void the_user_receives_a_list_of_all_their_payments() {
        // Write code here that turns the phrase above into concrete actions
        paymentList.forEach(p -> System.out.println(p.debtorId));
    }

}
