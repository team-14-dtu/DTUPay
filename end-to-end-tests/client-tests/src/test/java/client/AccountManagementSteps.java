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

    private Response registrationResponse;
    private Response retirementResponse;

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

        new AccountsClient().retireUser(ourCPR);
    }

    @Given("an user with first name {string}, last name {string} and account with balance {int}")
    public void aUserWithNameBankAccountAndCpr(String firstName, String lastName, Integer balance) throws BankServiceException_Exception {
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

    @When("the {string} registers with DTU Pay")
    public void theUserRegistersWithDTUPay(String accountType) {
        registrationResponse = app.registerUser(
                bankAccountId,
                user.getCprNumber(),
                user.getFirstName() + " " + user.getLastName(),
                accountType.equals("merchant")
        );
    }

    @Then("a customer is created and has some customer ID")
    public void aCustomerIsCreatedAndHasSomeCustomerID() {
        Assert.assertEquals(200, registrationResponse.getStatus());
        var customerId = registrationResponse.readEntity(UUID.class);
        Assert.assertNotNull(customerId);
    }


    @Then("an registration error message is returned saying {string}")
    public void anErrorMessageIsReturnedSaying(String message) {
        Assert.assertEquals(400, registrationResponse.getStatus());
        var responseMessage = registrationResponse.readEntity(String.class);
        Assert.assertEquals(message, responseMessage);
    }

    @Given("a {string} registered in DTU Pay")
    public void aRegisteredInDTUPay(String userType) throws BankServiceException_Exception {
        aUserWithNameBankAccountAndCpr("Emmanuel","Ryom",(1000000000));
        theUserRegistersWithDTUPay(userType);
    }

    @When("the user retires from DTU Pay")
    public void theCustomerRetiresFromDTUPay() {
        retirementResponse = app.retireUser(ourCPR);
    }

    @Then("the response is successful")
    public void theResponseIsSuccessful() {
        Assert.assertEquals(200,retirementResponse.getStatus());
    }

    @Given("a user who is not registered")
    public void aUserWhoIsNotRegistered() {

    }

    @Then("a retirement error message is returned saying {string}")
    public void aRetirementErrorMessageIsReturnedSaying(String message) {
        Assert.assertEquals(400, retirementResponse.getStatus());
        Assert.assertEquals(message, retirementResponse.readEntity(String.class));
    }
}
