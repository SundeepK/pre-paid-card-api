Feature:
  AS a consumer of a pre paid card
  I want to interact with merchant api
  SO that I can make merchants, get merchants and fetch balance

  Scenario: Api creates new merchant
    When I POST merchants:
      | name   |
      | amazon |
    Then response is a "200"
    And the JSON should be:
    """
     {
        "merchantId": 1,
        "name": "amazon"
     }
    """
    When I GET merchants/1/balance
    Then response is a "200"
    And the JSON should be:
    """
     {
        "merchantId": 1,
        "amount": 0.0,
        "nonce": 0
     }
    """

  Scenario: Api returns not found for unknown merchant
    When I GET merchants/1/balance
    Then response is a "404"
