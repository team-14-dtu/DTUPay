Feature: token management feature

  Scenario: Successful token generation
    Given a customer with no tokens
    When the customer requests 4 tokens
    Then the customer now has 4 tokens
    When the customer requests 2 tokens
    Then the customer now has 4 tokens
    And an error message is returned saying "Customer has 4 tokens and can therefore not request more"

  Scenario: Successful token generation
    Given a customer with no tokens
    When the customer requests 6 tokens
    Then the customer now has 0 tokens
    And an error message is returned saying "Requests of more than 5 tokens are not allowed"






