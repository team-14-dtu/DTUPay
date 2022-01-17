Feature: registration feature

  Scenario: Account is created on registration request event
    Given there is a bank account with id "123" and we want to create a customer with cpr "000-111", name "Petr Kubes" and bankAccount "123"
    When event arrives requesting creation
    Then a customer is created and an event published

  Scenario: Account is not created on registration request event when there is no bank account
    Given there is a bank account with id "000" and we want to create a customer with cpr "000-111", name "Petr Kubes" and bankAccount "123"
    When event arrives requesting creation
    Then an error event is received with message "User was not created, bank account doesn't exist"