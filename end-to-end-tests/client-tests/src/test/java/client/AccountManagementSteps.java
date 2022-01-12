package client;

import dk.dtu.team14.App;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;
import generated.dtu.ws.fastmoney.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.math.BigDecimal;

public class AccountManagementSteps {

    private final BankService bank = new BankServiceService().getBankServicePort();
    private final App app = new App("http://localhost:8080");

    User user;
    String bankAccountId;
    String customerId;


    @Given("a customer with first name {string}, last name {string}, cpr {string} and account with balance {int}")
    public void aCustomerWithNameBankAccountAndCpr(String firstName, String lastName, String cpr, Integer balance) throws BankServiceException_Exception {
        user = new User();
        user.setCprNumber(cpr);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        bankAccountId = bank.createAccountWithBalance(user, BigDecimal.valueOf(balance));
    }

    @When("the customer registers with DTU Pay")
    public void theCustomerRegistersWithDTUPay() {
        customerId = app.registerUser(
                bankAccountId,
                user.getCprNumber(),
                user.getFirstName()+" "+user.getLastName(),
                false
        );
    }

    @Then("a customer is created and has some customer ID")
    public void aCustomerIsCreatedAndHasSomeCustomerID() {
        Assert.assertNotNull(customerId);
    }
}
