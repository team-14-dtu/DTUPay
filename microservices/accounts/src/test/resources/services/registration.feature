Feature: registration feature

  Scenario: Account is created on registration request event
    Given there is a bank account with id "123"
    When event arrives requesting creation of customer with cpr "000-111", name "Petr Kubes" and bankAccount "123"
    Then a customer is created and an event published

  Scenario: Account is not created on registration request event when there is no bank account
    Given there is a bank account with id "000"
    When event arrives requesting creation of customer with cpr "000-111", name "Petr Kubes" and bankAccount "123"
    Then an error event is received with message "User was not created, bank account doesn't exist"

