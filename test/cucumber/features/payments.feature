Feature:
  AS a consumer of a pre paid card
  I want to interact with payment api
  SO that users can make purchases

  Scenario: Api allows payments and withdraws for merchants
    Given I POST cards:
      | cardNumber       | cvv | firstName | lastName | expireYear | expireMonth |
      | 5595301793561145 | 760 | Bob       | John     | 2020       | 2           |
    And I POST cards/deposit:
      | cardId | amount | nonce |
      | 1      | 10.50  | 0     |
    And I POST merchants:
      | name   |
      | amazon |
    When I POST payments:
      | cardId | merchantId | amount | nonce | reason      |
      | 1      | 1          | 10     | 1     | Rubber duck |
    Then response is a "200"
    And the JSON should be:
    """
     {
        "amount": 10.0,
        "cardId": 1,
        "merchantId": 1,
        "transactionId": 2,
        "reason": "Rubber duck",
        "type": "PAYMENT"
     }
    """
    When I GET earmarked/cardId/1/merchant/1
    Then response is a "200"
    And the JSON should be:
    """
     {
        "amount": 10.0,
        "cardId": 1,
        "merchantId": 1
     }
    """
    When I POST withdraws:
      | cardId | merchantId | amount | nonce |
      | 1      | 1          | 10     | 0     |
    Then response is a "200"
    And the JSON should be:
    """
     {
        "amount": 10.0,
        "merchantId": 1,
        "transactionId": 3,
        "type": "WITHDRAW"
     }
    """
    When I GET merchants/1/balance
    Then response is a "200"
    And the JSON should be:
    """
     {
        "amount": 10.0,
        "merchantId": 1,
        "nonce": 1
     }
    """

  Scenario: Api forbids withdrawing more than exists in ear marked balance
    Given I POST cards:
      | cardNumber       | cvv | firstName | lastName | expireYear | expireMonth |
      | 5595301793561145 | 760 | Bob       | John     | 2020       | 2           |
    And I POST cards/deposit:
      | cardId | amount | nonce |
      | 1      | 10.50  | 0     |
    And I POST merchants:
      | name   |
      | amazon |
    And I POST payments:
      | cardId | merchantId | amount | nonce | reason      |
      | 1      | 1          | 10     | 1     | Rubber duck |
    When I POST withdraws:
      | cardId | merchantId | amount | nonce |
      | 1      | 1          | 100    | 0     |
    Then response is a "400"
    And the JSON should be:
    """
    {
      "error": "Payment results in negative ear mark balance."
    }
    """

  Scenario: Api allows partial withdraws
    Given I POST cards:
      | cardNumber       | cvv | firstName | lastName | expireYear | expireMonth |
      | 5595301793561145 | 760 | Bob       | John     | 2020       | 2           |
    And I POST cards/deposit:
      | cardId | amount | nonce |
      | 1      | 500    | 0     |
    And I POST merchants:
      | name   |
      | amazon |
    And I POST payments:
      | cardId | merchantId | amount | nonce | reason |
      | 1      | 1          | 400    | 1     | TV     |
    When I POST withdraws:
      | cardId | merchantId | amount | nonce |
      | 1      | 1          | 300    | 0     |
    Then response is a "200"
    When I GET merchants/1/balance
    Then response is a "200"
    And the JSON should be:
    """
     {
        "amount": 300.0,
        "merchantId": 1,
        "nonce": 1
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
