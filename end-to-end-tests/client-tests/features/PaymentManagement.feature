Feature: payment management feature

	Scenario: Successful payment
		Given a customer with a bank account with balance 1000
		And a merchant with a bank account with balance 1000
		And the merchant asks the customer for payment of 100 kr and description "Receipt: Ice cream"
#		And the customer gives the merchant their tokenId through NFC "5a7b69d2-c12f-41d8-92d8-6535b9e46655"
		When the merchant requests the payment to DTUPay
		Then the payment is successful
		And the balance of the customer at the bank is 900 kr
		And the balance of the merchant at the bank is 1100 kr

	Scenario: Unsuccessful payment due to insufficient balance
		Given a customer with a bank account with balance 100
		And a merchant with a bank account with balance 1000
		And the merchant asks the customer for payment of 200 kr and description "Receipt: Ice cream"
		#And the customer gives the merchant their tokenId through NFC "5a7b69d2-c12f-41d8-92d8-6535b9e46655"
		When the merchant requests the payment to DTUPay
		Then the payment is unsuccessful
		And an error message is returned saying "Debtor balance will be negative" payment

	Scenario: Unsuccessful payment due to invalid amount
		Given a customer with a bank account with balance 1000
		And a merchant with a bank account with balance 1000
		And the merchant asks the customer for payment of -200 kr and description "Receipt: Ice cream"
		#And the customer gives the merchant their tokenId through NFC "5a7b69d2-c12f-41d8-92d8-6535b9e46655"
		When the merchant requests the payment to DTUPay
		Then the payment is unsuccessful
		And an error message is returned saying "Amount must be positive" payment

		#TODO Scenario: for invalid token and non existent merchant (should be received from other services)

	Scenario: Successful history retrieval by customer
		Given a payment customer with id "0da48506-78b1-11ec-90d6-0242ac120003"
		When the customer requests his payments
		Then the customer receives a list of all their payments

	Scenario: Successful history retrieval by merchant
		Given a merchant with id "1e9e0f12-78b1-11ec-90d6-0242ac120003"
		When the merchant requests his payments
		Then the merchant receives a list of all their payments

	Scenario: Successful history retrieval by manager
		Given the manager
		When the manager requests all payments
		Then the manager receives a list of all payments







