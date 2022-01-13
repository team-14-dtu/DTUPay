Feature: registration feature

  Scenario: Account is created on registration request event
    Given there is a bank account with id "123"
    When event arrives requesting creation of customer with cpr "1111", name "Petr" and bankAccount "123"
    Then a customer is created and an event published