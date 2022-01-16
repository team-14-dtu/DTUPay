package client;

import dk.dtu.team14.App;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;
import generated.dtu.ws.fastmoney.User;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.math.BigDecimal;

public class AccountManagementSteps {

    private final BankService bank = new BankServiceService().getBankServicePort();
    private final App app = new App();

    User user;
    String bankAccountId;
    String customerId;

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

    @When("the customer registers with DTU Pay")
    public void theCustomerRegistersWithDTUPay() {
        customerId = app.registerUser(
                bankAccountId,
                user.getCprNumber(),
                user.getFirstName() + " " + user.getLastName(),
                false
        );
    }

    @Then("a customer is created and has some customer ID")
    public void aCustomerIsCreatedAndHasSomeCustomerID() {
        Assert.assertNotNull(customerId);
    }
}
