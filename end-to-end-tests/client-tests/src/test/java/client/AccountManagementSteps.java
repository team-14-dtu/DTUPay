package client;

import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.User;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.UUID;

public class AccountManagementSteps extends BaseSteps {

    protected String name;
    protected String cpr;
    protected UserType userType;
    protected String bankAccountId;
    private Response registrationResponse;
    private Response retirementResponse;
    // @author : Petr
    @Before
    public void deleteAccounts() {
        beforeStartClean();
    }
    // @author : Emmanuel
    @Given("a bank account {string} registered to cpr index {int} with balance {int}")
    public void aBankAccountRegisteredToCprIndexWithBalance(String bankAccountName, int cprIndex, int balance) throws BankServiceException_Exception {
        User user = new User();
        user.setFirstName("First");
        user.setLastName("Second");
        user.setCprNumber(ourCprs[cprIndex]);
        var bankAccountId = bank.createAccountWithBalance(user, BigDecimal.valueOf(balance));
        bankAccounts.put(bankAccountName, bankAccountId);
    }
    // @author : Petr
    @Given("a bank account {string} is fake")
    public void aBankAccountIsFake(String account) {
        bankAccounts.put(account, "fakefakefake");
    }
    // @author : Emmanuel
    @Given("an user with name {string}, cpr index {int} and a bank account {string} who is {string}")
    public void anUserWithNameCprIndexAndABankAccountWhoIs(String name, int cprIdx, String bankAccountName, String userType) {
        this.name = name;
        this.cpr = ourCprs[cprIdx];
        this.bankAccountId = bankAccounts.get(bankAccountName);
        this.userType = UserType.valueOf(userType);
    }
    // @author : Petr
    @Given("an user with name {string}, cpr index {int} and a non-existent bank account who is {string}")
    public void anUserWithNameCprIndexAndABankAccountWhoIs(String name, int cprIdx, String userType) {
        this.name = name;
        this.cpr = ourCprs[cprIdx];
        this.bankAccountId = "random non existent";
        this.userType = UserType.valueOf(userType);
    }
    // @author : Emmanuel
    @When("the user registers with DTU Pay")
    public void theUserRegistersWithDTUPay() {
        registrationResponse = app.registerUser(
                bankAccountId,
                cpr,
                name,
                userType == UserType.MERCHANT
        );
    }
    // @author : Petr
    @Then("the response is successful and return some ID")
    public void theResponseIsSuccessfulAndReturnSomeID() {
        Assert.assertEquals(200, registrationResponse.getStatus());
        var customerId = registrationResponse.readEntity(UUID.class);
        Assert.assertNotNull(customerId);
    }
    // @author : Emmanuel
    @Then("a registration error message is returned saying {string}")
    public void aRegistrationErrorMessageIsReturnedSaying(String message) {
        Assert.assertEquals(400, registrationResponse.getStatus());
        Assert.assertEquals(message, registrationResponse.readEntity(String.class));
    }
    // @author : Petr
    @When("the user retires from DTU Pay")
    public void theUserRetiresFromDTUPay() {
        retirementResponse = app.retireUser(cpr);
    }
    // @author : Emmanuel
    @Then("the retirement response is successful")
    public void theRetirementResponseIsSuccessful() {
        Assert.assertEquals(200, retirementResponse.getStatus());
    }
    // @author : Petr
    @Then("a retirement error message is returned saying {string}")
    public void aRetirementErrorMessageIsReturnedSaying(String message) {
        Assert.assertEquals(400, retirementResponse.getStatus());
        Assert.assertEquals(message, retirementResponse.readEntity(String.class));
    }
}
