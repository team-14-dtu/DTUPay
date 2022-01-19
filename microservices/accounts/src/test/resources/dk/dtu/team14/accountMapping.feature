Feature: Account Mapping

  Scenario: Successful bank account ID retrieval for customer
    Given a customer with a bank account ID "1234"
    When an event with his customer ID arrives
    Then an event with his customer ID and bank account ID "1234" is published
