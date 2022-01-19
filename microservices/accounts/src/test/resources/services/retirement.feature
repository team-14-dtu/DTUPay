Feature: registration feature

  Scenario: User is retired successfully
    Given there is a customer with cpr "000-123", name "Petr Kubes" and bankAccount "123"
    When event arrives requesting retirement of that user
    Then user is deleted and event published

  Scenario: Non-user attempts retirement
    Given there is a non-customer with cpr "000-112", name "Petr Tubes" and bankAccount "124"
    When event arrives requesting retirement of that user
    Then there is an error message saying "User was not registered"

  Scenario: User attempts retirement twice
    Given there is a customer with cpr "000-112", name "Petr Tubes" and bankAccount "124"
    When event arrives requesting retirement of that user
    Then user is deleted and event published
    When event arrives requesting retirement of that user
    Then there is an error message saying "User was not registered"