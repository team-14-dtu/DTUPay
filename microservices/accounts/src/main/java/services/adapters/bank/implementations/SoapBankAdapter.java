package services.adapters.bank.implementations;

import generated.dtu.ws.fastmoney.BankService;
import generated.dtu.ws.fastmoney.BankServiceService;
import services.adapters.bank.Bank;

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
