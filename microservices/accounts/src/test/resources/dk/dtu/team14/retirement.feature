Feature: registration feature

  Scenario: User is retired successfully
    Given there is a customer with id "e6a65602-f86c-4afc-b963-8325495dd16c" cpr "000-111", name "Petr Kubes" and bankAccount "123"
    When event arrives requesting retirement of that user
    Then user is deleted and event published