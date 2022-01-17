package dk.dtu.team14.adapters.bank.implementations;

import dk.dtu.team14.adapters.bank.Bank;

public class SoapBankAdapter implements Bank {
    @Override
    public boolean doesBankAccountExist(String bankAccountId) {
        return false;
    }
}
