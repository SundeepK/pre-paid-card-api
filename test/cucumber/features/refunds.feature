Feature:
  AS a consumer of a pre paid card
  I want to interact with refunds api
  SO that merchants can refund customers

  Scenario: Api allows refunds
    When I POST cards:
      | cardNumber       | cvv | firstName | lastName | expireYear | expireMonth |
      | 5595301793561145 | 760 | Bob       | John     | 2020       | 2           |
    When I POST cards/deposit:
      | cardId | amount | nonce |
      | 1      | 500    | 0     |
    When I POST merchants:
      | name   |
      | amazon |
    When I POST payments:
      | cardId | merchantId | amount | nonce | reason |
      | 1      | 1          | 400    | 1     | TV     |
    When I POST refunds:
      | cardId | merchantId | amount | nonce |
      | 1      | 1          | 300    | 2     |
    Then response is a "200"
    When I GET cards/1/balance
    Then response is a "200"
    And the JSON should be:
    """
     {
        "amount": 400.0,
        "cardId": 1,
        "nonce": 3
     }
    """
    When I GET earmarked/cardId/1/merchant/1
    Then response is a "200"
    And the JSON should be:
    """
     {
        "amount": 100.0,
        "cardId": 1,
        "merchantId": 1
     }
    """

  Scenario: Api forbids refunding too much
    When I POST cards:
      | cardNumber       | cvv | firstName | lastName | expireYear | expireMonth |
      | 5595301793561145 | 760 | Bob       | John     | 2020       | 2           |
    When I POST cards/deposit:
      | cardId | amount | nonce |
      | 1      | 100    | 0     |
    When I POST merchants:
      | name   |
      | amazon |
    When I POST payments:
      | cardId | merchantId | amount | nonce | reason |
      | 1      | 1          | 50     | 1     | TV     |
    When I POST refunds:
      | cardId | merchantId | amount | nonce |
      | 1      | 1          | 200    | 2     |
    Then response is a "400"
    And the JSON should be:
    """
     {
        "error": "Payment results in negative ear mark balance."
     }
    """