package dk.dtu.team14.adapters.bank;

import generated.dtu.ws.fastmoney.BankServiceException_Exception;

public interface Bank {
    boolean doesBankAccountExist(String bankAccountId);
}
