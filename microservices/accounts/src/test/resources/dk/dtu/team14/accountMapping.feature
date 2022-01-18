Feature: Account Mapping

  Scenario: Successful bank account ID retrieval for customer
    Given a customer with ID "dbede105-77dc-4bb8-917b-2cb63090d17e" and bank account ID "1234"
    When an event with customer ID "dbede105-77dc-4bb8-917b-2cb63090d17e" arrives
    Then an event with customer ID "dbede105-77dc-4bb8-917b-2cb63090d17e" and bank account ID "1234" is published
