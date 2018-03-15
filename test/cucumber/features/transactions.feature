Feature:
  AS a consumer of a pre paid card
  I want to interact with transactions api
  SO that I can see all my transactions

  Scenario: Api allows payments and withdraws for merchants
    Given I POST cards:
      | cardNumber       | cvv | firstName | lastName | expireYear | expireMonth |
      | 5595301793561145 | 760 | Bob       | John     | 2020       | 2           |
      | 5562045637601828 | 260 | Gabe      | Newell   | 2022       | 4           |
    And I POST cards/deposit:
      | cardId | amount | nonce |
      | 1      | 10     | 0     |
      | 1      | 150    | 1     |
    And I POST cards/deposit:
      | cardId | amount | nonce |
      | 2      | 1000   | 0     |
      | 2      | 20     | 1     |
    And I POST merchants:
      | name   |
      | amazon |
      | coffee |
    And I POST payments:
      | cardId | merchantId | amount | nonce | reason      |
      | 2      | 1          | 10     | 2     | Rubber duck |
    Then response is a "200"
    And I POST withdraws:
      | cardId | merchantId | amount | nonce |
      | 2      | 1          | 10     | 0     |
    When I GET cards/2/transactions
    Then response is a "200"
    And the JSON should be:
    """
    [
      {
        "amount": 1000.0,
        "cardId": 2,
        "transactionId": 3,
        "type": "DEPOSIT"
      },
      {
        "amount": 20.0,
        "cardId": 2,
        "transactionId": 4,
        "type": "DEPOSIT"
      },
      {
        "amount": 10.0,
        "cardId": 2,
        "merchantId": 1,
        "reason": "Rubber duck",
        "transactionId": 5,
        "type": "PAYMENT"
      }
    ]
    """
