Feature: history feature

  Scenario: Restricted payment history list is received on paymenthistory request event for user
    Given there is a user with id "customerId1" and type "CUSTOMER"
    When an event arrives requesting the users payment history
    Then a restricted payment history list for the user is returned and an event is published
