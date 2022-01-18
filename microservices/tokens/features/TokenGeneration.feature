Feature: Token generation feature

	Scenario: Token generation success
		Given a customer with customerId "8af3b9e2-7842-11ec-90d6-0242ac120003" and 0 tokens
		When a "tokens_requested" event is received for 3 tokens and customerId "8af3b9e2-7842-11ec-90d6-0242ac120003"
		Then the "tokens_replied" event is sent
		And customerId "8af3b9e2-7842-11ec-90d6-0242ac120003" is now associated with 3 tokens

	Scenario: Token generation unsuccessful
		Given a customer with customerId "f3d0202c-7842-11ec-90d6-0242ac120003" and 2 tokens
		When a "tokens_requested" event is received for 3 tokens and customerId "f3d0202c-7842-11ec-90d6-0242ac120003"
		Then the "tokens_replied" event is sent
        And customerId "f3d0202c-7842-11ec-90d6-0242ac120003" is now associated with 2 tokens

