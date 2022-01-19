Feature: Account Mapping

  Scenario: Successful bank account ID retrieval for customer
    Given a customer with a bank account ID "1234"
    When an event with his customer ID arrives
    Then an event with his customer ID and bank account ID "1234" is published

  Scenario: Unsuccessful bank account ID retrieval for customer
    Given a customer with a bank account ID "1234"
    When an event with random customer ID arrives
    Then an event with error message "User not found" is published

  Scenario: Bank account request for unknown user
    When an event with unknown customer ID arrives
    Then an event with error message "User not found" is published
    #TODO add the correct error message
