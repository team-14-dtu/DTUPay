Feature: Token generation feature

	Scenario: Token generation success
		Given a customer with customerId "cid1" and 0 tokens
		When a "requestTokensEvent" event is received for 3 tokens and customerId "cid1"
		Then the "replyTokensEvent" event is sent
		And customerId "cid1" with now is associated with 3 tokens


#	Scenario: Token generation success
#		Given a customer with customerId "cid1"
#		And the customer has 1 token
#		When the customer request 3 tokens
#		Then the "requestTokensEvent" event is sent
#		When the "replyTokensEvent" event is sent with a list of tokens
#		Then the customer now has 4 tokens

#	Scenario: Token generation failure
#		Given a customer with customerId "cid2"
#		And the customer has 2 token
#		When the customer request 3 tokens
#		Then the "requestTokensEvent" event is returning an error-message saying: "Error: You can't request tokens if you have more than one token"
#		When the "ReplyTokensEvent" event is sent with a list of tokens
#		Then the customer now has 2 tokens
