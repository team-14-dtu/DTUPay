Feature: Payment service feature

  Scenario: Payment is registered on pay request event
    Given a valid token with id "ea0c95a8-794f-11ec-90d6-0242ac120003"
    And a merchant with id "2530ea3a-7950-11ec-90d6-0242ac120003"
    And an amount of 100 with description "Beers with friends"
    When an event arrives requesting payment
    And a payment is registered and an event is published

  Scenario: Successful payment history received for a customer request
    Given a customer with id "14f5c064-795a-11ec-90d6-0242ac120003"
    And a payment exists for the customer
    When an event arrives requesting the customers payment history
    Then the customer payment history is fetched from the payment database and an event is published

  Scenario: Successful payment history received for a merchant request
    Given a merchant with id "fd9cf51e-7962-11ec-90d6-0242ac120003"
    And a payment exists for the merchant
    When an event arrives requesting the merchants payment history
    Then the merchant payment history is fetched from the payment database and an event is published

  Scenario: Successful payment history received for a manager request
    Given a payment exists in the payment database
    When an event arrives requesting the managers payment history
    Then the manager payment history is fetched from the payment database and an event is published


