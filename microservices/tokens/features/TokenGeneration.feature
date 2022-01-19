Feature: Token generation feature

	Scenario: Token generation success
		Given a customer with customerId "cid1" and 0 tokens
		When a "tokens_requested" event is received for 3 tokens and customerId "cid1"
		Then the "tokens_replied" event is sent
		And customerId "cid1" with now is associated with 3 tokens

	Scenario: Token generation unsuccessful
		Given a customer with customerId "cid2" and 2 tokens
		When a "tokens_requested" event is received for 3 tokens and customerId "cid2"
		Then the "tokens_replied" event is sent
        And customerId "cid2" with now is associated with 2 tokens
		And an error message is received saying "Customer has 2 tokens and is not allowed to request more"


