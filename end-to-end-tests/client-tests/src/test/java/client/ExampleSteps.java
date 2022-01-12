package client;

import dk.dtu.team14.App;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import rest.User;

public class ExampleSteps {

    private static String baseUrl = "http://localhost:8080";

    private User response;

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
        Assert.assertEquals("Petr", response.getUserName());
    }
}
