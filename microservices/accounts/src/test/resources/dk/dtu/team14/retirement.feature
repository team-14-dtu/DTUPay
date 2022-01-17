Feature: registration feature

  Scenario: User is retired successfully
    Given there is a customer with id "1111" cpr "000-111", name "Petr Kubes" and bankAccount "123"
    When event arrives requesting retirement of that user
    Then costumer is deleted and event published