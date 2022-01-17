package dk.dtu.team14.adapters.bank.implementations;

import dk.dtu.team14.adapters.bank.Bank;
import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceException_Exception;
import generated.dtu.ws.fastmoney.BankServiceService;

public class SoapBankAdapter implements Bank {
    @Override
    public boolean doesBankAccountExist(String bankAccountId) {

        BankService bank = new BankServiceService().getBankServicePort();

        try {
            var exists = bank.getAccount(bankAccountId);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
