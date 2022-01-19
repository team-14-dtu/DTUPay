Feature: payment management feature

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







