Feature: payment feature

	Scenario: Successful payment
		Given a customer with id "customerId1" with a bank account with balance 1000
		And a merchant with id "merchantId1" with a bank account with balance 2000
		When the merchant initiates a payment for 100 kr and description "Receipt: Ice cream"
		And the customer gives the merchant their "tokenId1"
		Then the merchant requests the payment to DTUPay
		When the payment is successful
		Then the balance of the customer at the bank is 900 kr
		And the balance of the merchant at the bank is 2100 kr

	#Scenario: Unsuccessful payment
	#	Given a customer with a bank account with balance 100
	#	And a merchant with a bank account with balance 2000
	#	When the merchant initiates a payment for 200 kr by the customer
	#	Then the payment is unsuccessful
	#	And an error message is returned saying "Error: customer does not have enough money in da bank"

	Scenario: Successful history retrieval by customer
		Given a payment customer with id "customerId2"
		When the user requests his payments
		Then the user receives a list of all their payments

	#Scenario: Successful history retrieval by merchant
	#	Given a merchant with id "mid1"
	#	When the user requests his payments
	#	Then the user receives a list of all their payments

	Scenario: Successful history retrieval by manager
		Given the manager
		When the manager requests all payments
		Then the manager receives a list of all payments







