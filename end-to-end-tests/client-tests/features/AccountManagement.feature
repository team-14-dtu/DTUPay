Feature: account management feature

	Scenario: Successful account creation with DTU pay
		Given a customer with first name "Yowk", last name "ABC" and account with balance 1212
		When the customer registers with DTU Pay
		Then a customer is created and has some customer ID

	Scenario: Unsuccessful account creation with DTU pay
		Given a customer with name "Yorn", cpr "010101-1111" and bank account "123" which does not exist
		When the customer registers with DTU Pay
		Then an error message is returned saying "User was not created, bank account doesn't exist"

	Scenario: Successful retirement of customer in DTU pay
		Given a customer registered in DTU Pay
		When the customer retires from DTU Pay
		Then the response is successful
#
#	Scenario: Successful retirement of merchant in DTU pay
#		Given a merchant with id "mid"
#		When the user retires from DTU Pay
#		Then the user no longer exist in DTU Pay
#
#	Scenario: Unsuccessful retirement of user in DTU pay
#		Given a user with id "id does not exist"
#		When the user retires from DTU Pay
#		Then an error message is returned saying "Error: user does not exist in DTU Pay"




