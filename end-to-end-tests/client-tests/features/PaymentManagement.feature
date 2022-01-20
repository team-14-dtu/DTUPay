Feature: payment management feature

	#Make sure this test is in the beginning (so we dont fill out payment info in the db)
#	Scenario: Unsuccessful history retrieval by manager due to no payment history
#		Given the manager
#		When the manager requests all payments
#		Then an error message is returned saying "There is no payment history" manager history

	Scenario: Successful payment
		Given a customer with a bank account with balance 1000
		And a merchant with a bank account with balance 1000
		And the merchant asks the customer for payment of 100 kr and description "Receipt: Ice cream"
		When the merchant requests the payment to DTUPay
		Then the payment is successful
		And the balance of the customer at the bank is 900 kr
		And the balance of the merchant at the bank is 1100 kr

	Scenario: Unsuccessful payment due to insufficient balance
		Given a customer with a bank account with balance 100
		And a merchant with a bank account with balance 1000
		And the merchant asks the customer for payment of 200 kr and description "Receipt: Ice cream"
		When the merchant requests the payment to DTUPay
		Then the payment is unsuccessful
		And an error message is returned saying "Insufficient balance" payment
		And the balance of the customer at the bank is 100 kr
		And the balance of the merchant at the bank is 1000 kr

	Scenario: Unsuccessful payment due to invalid amount
		Given a customer with a bank account with balance 1000
		And a merchant with a bank account with balance 1000
		And the merchant asks the customer for payment of -200 kr and description "Receipt: Ice cream"
		When the merchant requests the payment to DTUPay
		Then the payment is unsuccessful
		And an error message is returned saying "Payment amount must be positive" payment
		And the balance of the customer at the bank is 1000 kr
		And the balance of the merchant at the bank is 1000 kr

	Scenario: Unsuccessful payment due to invalid token
		Given a customer with a bank account with balance 1000
		And a merchant with a bank account with balance 1000
		And the merchant asks the customer for payment of 100 kr and description "Receipt: Ice cream"
		And the customer gives the merchant an invalid tokenId through NFC
		When the merchant requests the payment to DTUPay
		Then the payment is unsuccessful
		And an error message is returned saying "Customer is not found" payment
		And the balance of the customer at the bank is 1000 kr
		And the balance of the merchant at the bank is 1000 kr

	Scenario: Unsuccessful payment due to non existent merchant
		Given a customer with a bank account with balance 1000
		And a merchant that does not exist
		And the merchant asks the customer for payment of 200 kr and description "Receipt: Ice cream"
		When the merchant requests the payment to DTUPay
		Then the payment is unsuccessful
		And an error message is returned saying "User not found" payment

	Scenario: Successful history retrieval by customer
		Given a customer with a bank account with balance 1000
		And a merchant with a bank account with balance 1000
		And the merchant asks the customer for payment of 100 kr and description "Receipt: Ice cream"
		When the merchant requests the payment to DTUPay
		Then the payment is successful
		When the customer requests his payments
		Then the customer receives their payments

	Scenario: Successful history retrieval by merchant
		Given a customer with a bank account with balance 1000
		And a merchant with a bank account with balance 1000
		And the merchant asks the customer for payment of 100 kr and description "Receipt: Ice cream"
		When the merchant requests the payment to DTUPay
		Then the payment is successful
		When the merchant requests his payments
		Then the merchant receives a list of all their payments

	Scenario: Successful history retrieval by manager
		Given the manager
		When the manager requests all payments
		Then the manager receives a list of all payments

	Scenario: Unsuccessful history retrieval by customer due to non existing customer
		Given a customer that does not exist
		When the customer requests his payments
		Then an error message is returned saying "Customer does not exist" customer history

	Scenario: Unsuccessful history retrieval by merchant due to non existing merchant
		Given a merchant that does not exist
		When the merchant requests his payments
		Then an error message is returned saying "Merchant does not exist" merchant history

	Scenario: Unsuccessful history retrieval by customer due to no customer payment history
		Given a customer with a bank account with balance 1000
		When the customer requests his payments
		Then an error message is returned saying "Customer has no payment history" customer history

	Scenario: Unsuccessful history retrieval by merchant due to no merchant payment history
		Given a merchant with a bank account with balance 1000
		When the merchant requests his payments
		Then an error message is returned saying "Merchant has no payment history" merchant history

