Feature: Token verification feature

  Scenario: Token verification success
    Given a customer with customerId "1916ccb8-7862-11ec-90d6-0242ac120003" who is in possession of a token with tokenId "24eb1466-7864-11ec-90d6-0242ac120003"
    When the "customerId_from_token_requested" event is sent containing the tokenId
    Then the "bankaccountId_from_customerId_requested" event is received containing the customerId
    And the token has been invalidated in the database


  Scenario: Token verification fail
    Given an invalid tokenId "24eb1466-7864-11ec-90d6-0242ac120003"
    When the "customerId_from_token_requested" event is sent containing the tokenId
    Then the "bankaccountId_from_customerId_requested" event is received containing an error-message saying "Customer is not found"
