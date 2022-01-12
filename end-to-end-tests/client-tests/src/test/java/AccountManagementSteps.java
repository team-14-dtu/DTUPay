import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccountManagementSteps {

    @Given("a customer with name {string}, accountId {string} and cpr {string}")
    public void aCustomerWithNameAccountIdAndCpr(String name, String accountId, String cpr) {

    }

    @When("the customer registers with DTU Pay")
    public void theCustomerRegistersWithDTUPay() {

    }

    @Then("a customer is created with customer ID {string}")
    public void aCustomerIsCreatedWithCustomerID(String customerId) {

    }
}
