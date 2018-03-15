Feature:
  AS a consumer of a pre paid card
  I want to interact with card api
  SO that I can make cards, deposit money and fetch balance

  Scenario: Api creates new card
    When I POST cards:
      | cardNumber       | cvv | firstName | lastName | expireYear | expireMonth |
      | 5595301793561145 | 760 | Bob       | John     | 2020       | 2           |
    Then response is a "200"
    And the JSON should be:
    """
     {
        "cardId": 1,
        "firstName": "Bob",
        "lastName": "John"
     }
    """

  Scenario: Api doesn't create duplicate cards
    When I POST cards:
      | cardNumber       | cvv | firstName | lastName | expireYear | expireMonth |
      | 5595301793561145 | 760 | Bob       | John     | 2020       | 2           |
    Then response is a "200"
    And the JSON should be:
    """
     {
        "cardId": 1,
        "firstName": "Bob",
        "lastName": "John"
     }
    """
    When I POST cards:
      | cardNumber       | cvv | firstName | lastName | expireYear | expireMonth |
      | 5595301793561145 | 760 | Bob       | John     | 2020       | 2           |
    Then response is a "400"
    And the JSON should be:
    """
     {
        "error": "Card already exists."
     }
    """

  Scenario: Api allows deposits
    When I POST cards:
      | cardNumber       | cvv | firstName | lastName | expireYear | expireMonth |
      | 5595301793561145 | 760 | Bob       | John     | 2020       | 2           |
    Then response is a "200"
    When I GET cards/1/balance
    Then response is a "200"
    And the JSON should be:
    """
     {
        "cardId": 1,
        "amount": 0.0,
        "nonce": 0
     }
    """
    When I POST cards/deposit:
      | cardId | amount | nonce |
      | 1      | 10.50  | 0     |
    Then response is a "200"
    And the JSON should be:
    """
     {
        "amount": 10.5,
        "cardId": 1,
        "transactionId": 1,
        "type": "DEPOSIT"
     }
    """
    When I POST cards/deposit:
      | cardId | amount | nonce |
      | 1      | 0.49   | 1     |
    Then response is a "200"
    And the JSON should be:
    """
     {
        "amount": 0.49,
        "cardId": 1,
        "transactionId": 2,
        "type": "DEPOSIT"
     }
    """
    When I GET cards/1/balance
    Then response is a "200"
    And the JSON should be:
    """
     {
        "cardId": 1,
        "amount": 10.99,
        "nonce": 2
     }
    """

  Scenario: Api returns error on incorrect nonce on deposit
    When I POST cards:
      | cardNumber       | cvv | firstName | lastName | expireYear | expireMonth |
      | 5595301793561145 | 760 | Bob       | John     | 2020       | 2           |
    Then response is a "200"
    When I POST cards/deposit:
      | cardId | amount | nonce |
      | 1      | 20.00  | 1     |
    Then response is a "400"
    And the JSON should be:
    """
    {
      "error": "Invalid nonce supplied"
    }
    """
