Feature: registration feature

  Scenario: User is retired successfully
    Given there is a customer with cpr "000-123", name "Petr Kubes" and bankAccount "123"
    When event arrives requesting retirement of that user
    Then user is deleted and event published