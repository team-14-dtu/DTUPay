package client;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import rest.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

public class ExampleSteps {

    private static String url = "http://localhost:8080";

    private User response;

    @Given("nothing")
    public void nothing() {
    }

    @When("we go to the website")
    public void weGoToTheWebsite() {
        Client client = ClientBuilder.newClient();
        WebTarget r = client.target(url);
        response = r.request().get(User.class);
    }

    @Then("it does not show error")
    public void itDoesNotShowError() {
        Assert.assertEquals("Petr", response.getName());
    }
}
