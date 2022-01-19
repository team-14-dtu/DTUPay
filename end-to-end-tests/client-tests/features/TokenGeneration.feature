Feature: token management feature

	Scenario: Successful token generation
		Given a customer with no tokens
		When the customer requests 4 tokens
		Then the customer now has 4 tokens
		When the customer requests 2 tokens
		Then the customer now has 4 tokens
		And an error message is returned saying "Customer has 4 tokens and is not allowed to request more"






