package client;

import services.Manager.AccountsManagerClient;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BaseSteps {
    protected final BankService bank = new BankServiceService().getBankServicePort();
    protected final AccountsManagerClient app = new AccountsManagerClient();

    protected final String[] ourCprs = new String[]{"83839-1823", "10312-31242", "1924878912-3123", "713025987-323"};
    protected final Map<String, String> bankAccounts = new HashMap<>();

    protected void beforeStartClean() {
        bank.getAccounts()
                .stream()
                .filter(accountInfo ->
                        Arrays.stream(ourCprs).anyMatch(cpr ->
                                Objects.equals(accountInfo.getUser().getCprNumber(), cpr)
                        )
                ).forEach(accountInfo -> {
                    try {
                        bank.retireAccount(accountInfo.getAccountId());
                    } catch (BankServiceException_Exception e) {
                        e.printStackTrace();
                    }
                });

        for (String ourCpr : ourCprs) {
            new AccountsManagerClient().retireUser(ourCpr);
        }

        bankAccounts.clear();
    }

    enum UserType {
        CUSTOMER,
        MERCHANT
    }
}
