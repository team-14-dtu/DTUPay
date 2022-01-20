Feature: Payment service feature

  Scenario: Payment is registered on pay request event
    Given a valid token with id "ea0c95a8-794f-11ec-90d6-0242ac120003"
    And a merchant with id "2530ea3a-7950-11ec-90d6-0242ac120003"
    And an amount of 100 with description "Beers with friends"
    When an event arrives requesting payment
    Then a payment is registered and an event is published

  Scenario: Unsuccessful payment due to bank exception
    Given a valid token with id "ea0c95a8-794f-11ec-90d6-0242ac120003"
    And a merchant with id "2530ea3a-7950-11ec-90d6-0242ac120003"
    And an amount of 100 with description "Beers with friends"
    When an event arrives requesting payment which fails due to a bank exception
    Then a payment is not registered and an error event with the string "BankService failure" is published

  Scenario: Unsuccessful payment due to invalid merchant ID
    Given a valid token with id "ea0c95a8-794f-11ec-90d6-0242ac120004"
    And a non existing merchant
    And an amount of 200 with description "Beers with colleges"
    When an event arrives requesting payment which will fail due to a non existing merchant
    Then a payment is not registered and an error event with the string "User not found" is published

  Scenario: Unsuccessful payment due to invalid token
    Given a invalid token
    And a merchant with id "2530ea3a-7950-11ec-90d6-0242ac120004"
    And an amount of 300 with description "Beers with proffesors"
    When an event arrives requesting payment which will fail due to an invalid token
    Then a payment is not registered and an error event with the string "Customer is not found" is published

  Scenario: Unsuccessful payment due to non-positive amount
    Given a valid token with id "ea0c95a8-794f-11ec-90d6-0242ac120005"
    And a merchant with id "2530ea3a-7950-11ec-90d6-0242ac120005"
    And a negative amount of -400 with description "Beers with family"
    When an event arrives requesting payment
    Then a payment is not registered and an error event with the string "Payment amount must be positive" is published

  Scenario: Unsuccessful payment due to customer not having enough balance
    Given a valid token with id "ea0c95a8-794f-11ec-90d6-0242ac120006"
    And a merchant with id "2530ea3a-7950-11ec-90d6-0242ac120006"
    And a too big amount of 1000 with description "Beers with students"
    When an event arrives requesting payment which will fail due to insufficient funds
    Then a payment is not registered and an error event with the string "Insufficient balance" is published

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

  Scenario: Unsuccessful payment history due to non existing customer
    Given a non existing customer
    When an event arrives requesting the customers payment history which will fail due to cant find user
    Then an error event for the customer with the message "Customer does not exist" is published

  Scenario: Unsuccessful payment history due to non existing merchant
    Given a non existing merchant
    When an event arrives requesting the merchants payment history which will fail due to cant find user
    Then an error event for the merchant with the message "Merchant does not exist" is published

  Scenario: Unsuccessful payment history due to empty customer history
    Given a customer with id "14f5c064-795a-11ec-90d6-0242ac120003"
    When an event arrives requesting the customers payment history
    Then an error event for the customer with the message "Customer has no payment history" is published

  Scenario: Unsuccessful payment history due to empty merchant history
    Given a merchant with id "14f5c064-795a-11ec-90d6-0242ac120003"
    When an event arrives requesting the merchants payment history
    Then an error event for the merchant with the message "Merchant has no payment history" is published

  Scenario: Unsuccessful payment history due to empty manager history
    Given a manager
    When an event arrives requesting the managers payment history
    Then an error event for the manager with the message "There is no payment history" is published




