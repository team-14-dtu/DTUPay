Feature: account management feature

  Scenario: Successful account creation with DTU pay
    Given a bank account "A" registered to cpr index 0 with balance 1212
    Given an user with name "Yowk", cpr index 0 and a bank account "A" who is "CUSTOMER"
    When the user registers with DTU Pay
    Then the response is successful and return some ID


  Scenario: Duplicate account creation with DTU pay
    Given a bank account "A" registered to cpr index 0 with balance 1212
    Given an user with name "Yowk", cpr index 0 and a bank account "A" who is "CUSTOMER"
    When the user registers with DTU Pay
    Then the response is successful and return some ID
    Given a bank account "B" registered to cpr index 1 with balance 6969
    Given an user with name "Bjowk", cpr index 1 and a bank account "A" who is "CUSTOMER"
    Given the user registers with DTU Pay
    Then a registration error message is returned saying "User could not be registered"


  Scenario: Unsuccessful account creation with DTU pay
    Given a bank account "A" is fake
    Given an user with name "Yowk", cpr index 0 and a bank account "A" who is "CUSTOMER"
    When the user registers with DTU Pay
    Then a registration error message is returned saying "User was not created, bank account doesn't exist"


  Scenario: Successful retirement of customer in DTU pay
    Given a bank account "A" registered to cpr index 0 with balance 1212
    Given an user with name "Yowk", cpr index 0 and a bank account "A" who is "CUSTOMER"
    When the user registers with DTU Pay
    Then the response is successful and return some ID
    When the user retires from DTU Pay
    Then the retirement response is successful

  Scenario: Successful retirement of merchant in DTU pay
    Given a bank account "A" registered to cpr index 0 with balance 1212
    Given an user with name "Yowk", cpr index 0 and a bank account "A" who is "MERCHANT"
    When the user registers with DTU Pay
    Then the response is successful and return some ID
    When the user retires from DTU Pay
    Then the retirement response is successful

  Scenario: Unsuccessful retirement of user in DTU pay
    Given an user with name "Yowk", cpr index 0 and a bank account "A" who is "CUSTOMER"
    When the user retires from DTU Pay
    Then a retirement error message is returned saying "User was not registered"




