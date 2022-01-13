Feature: registration feature

  Scenario: Account is created on registration request event
    Given there is a bank account with id "123" and we want to create a customer with cpr "000-111", name "Petr Kubes" and bankAccount "123"
    When event arrives requesting creation
    Then a customer is created and an event published