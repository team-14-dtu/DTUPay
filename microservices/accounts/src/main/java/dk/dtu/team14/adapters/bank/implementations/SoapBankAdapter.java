package dk.dtu.team14.adapters.bank.implementations;

import dk.dtu.team14.adapters.bank.Bank;

public class SoapBankAdapter implements Bank {
    @Override
    public boolean checkBankAccountExist(String bankAccountId) {
        return false;
    }
}
