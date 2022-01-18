package client;

import dk.dtu.team14.AccountsClient;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;
import generated.dtu.ws.fastmoney.User;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.UUID;

public class AccountManagementSteps {

    private final BankService bank = new BankServiceService().getBankServicePort();
    private final AccountsClient app = new AccountsClient();

    private User user;
    private String bankAccountId;

    private Response response;

    private final String ourCPR = "998877-0101";

    @Before
    public void deleteAccounts() {
        bank.getAccounts()
                .stream()
                .filter(accountInfo ->
                        accountInfo.getUser().getCprNumber().equals(ourCPR)
                ).forEach(accountInfo -> {
                    try {
                        bank.retireAccount(accountInfo.getAccountId());
                    } catch (BankServiceException_Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Given("a customer with first name {string}, last name {string} and account with balance {int}")
    public void aCustomerWithNameBankAccountAndCpr(String firstName, String lastName, Integer balance) throws BankServiceException_Exception {
        user = new User();
        user.setCprNumber(ourCPR);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        bankAccountId = bank.createAccountWithBalance(user, BigDecimal.valueOf(balance));
    }

    @Given("a customer with name {string}, cpr {string} and bank account {string} which does not exist")
    public void aCustomerWithNameCprAndBankAccountWhichDoesNotExist(String name, String cpr, String bankAccount) {
        user = new User();
        user.setCprNumber(cpr);
        user.setFirstName(name);
        user.setLastName("");
        bankAccountId = bankAccount;
    }

    @When("the customer registers with DTU Pay")
    public void theCustomerRegistersWithDTUPay() {
        response = app.registerUser(
                bankAccountId,
                user.getCprNumber(),
                user.getFirstName() + " " + user.getLastName(),
                false
        );
    }

    @Then("a customer is created and has some customer ID")
    public void aCustomerIsCreatedAndHasSomeCustomerID() {
        Assert.assertEquals(200, response.getStatus());
        var customerId = response.readEntity(UUID.class);
        Assert.assertNotNull(customerId);
    }


    @Then("an error message is returned saying {string}")
    public void anErrorMessageIsReturnedSaying(String message) {
        Assert.assertEquals(400, response.getStatus());
        var responseMessage = response.readEntity(String.class);
        Assert.assertEquals(message, responseMessage);
    }
}
