Feature: payment feature

	Scenario: Successful payment
		Given a customer with a bank account with balance 1000
		And a merchant with a bank account with balance 1000
		And the merchant asks the customer for payment of 100 kr and description "Receipt: Ice cream"
#		And the customer gives the merchant their tokenId through NFC "5a7b69d2-c12f-41d8-92d8-6535b9e46655"
		When the merchant requests the payment to DTUPay
		Then the payment is successful
		And the balance of the customer at the bank is 900 kr
		And the balance of the merchant at the bank is 1100 kr

	#Scenario: Unsuccessful payment
	#	Given a customer with a bank account with balance 100
	#	And a merchant with a bank account with balance 2000
	#	When the merchant initiates a payment for 200 kr by the customer
	#	Then the payment is unsuccessful
	#	And an error message is returned saying "Error: customer does not have enough money in da bank"

#	Scenario: Successful history retrieval by customer
#		Given a customer
#		When the customer requests his payments
#		Then the customer receives a list of all their payments
#
#	Scenario: Successful history retrieval by merchant
#		Given a merchant
#		When the merchant requests his earnings
#		Then the merchant receives a list of all their earnings

	#Scenario: Successful history retrieval by customer
	#	Given a payment customer with id "cid"
	#	When the user requests his payments
	#	Then the user receives a list of all their payments

	#Scenario: Successful history retrieval by merchant
	#	Given a merchant with id "mid1"
	#	When the user requests his payments
	#	Then the user receives a list of all their payments

	Scenario: Successful history retrieval by manager
		Given the manager
		When the manager requests all payments
		Then the manager receives a list of all payments







