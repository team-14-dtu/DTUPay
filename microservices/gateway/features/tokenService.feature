Feature: Token generation feature

  Scenario: Token generation success
		Given a customer with customerId "cid1"
		And the customer has 0 token
		When the customer request 3 tokens
		Then the "requestTokensEvent" event is sent
		When the "replyTokensEvent" event is received with a list of 3 tokens
		Then the customer now has 3 tokens


	Scenario: Token generation unsuccessful
		Given a customer with customerId "cid-manyTokens"
		And the customer has 2 token
		When the customer request 4 tokens
		Then the "requestTokensEvent" event is sent
		When the "replyTokensEvent" event is received with a list of 2 tokens
		Then the customer now has 2 tokens

#	Scenario: Token generation failure
#		Given a customer with customerId "cid2"
#		And the customer has 2 token
#		When the customer request 3 tokens
#		Then the "requestTokensEvent" event is returning an error-message saying: "Error: You can't request tokens if you have more than one token"
#		When the "ReplyTokensEvent" event is received with a list of tokens
#		Then the customer now has 2 tokens