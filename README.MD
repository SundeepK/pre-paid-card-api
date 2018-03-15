# Pre paid card api

This api provides a simple pre paid card mvp, where cards can be created and loaded up with money. Those funds can then be used to make payments to a an existing merchant which are earmarked. At which point, funds can be refunded or withdrawn to a merchant account.

Each payment, deposit, withdraw results in a new transaction which currently can only be fetch for cards and not merchants. This can be easily added however.

The api uses an in memory database, so restarting will lose data. Changing the database is however simple, since any sql based databased can be plugged in via spring properties and configured.

## Requirements
Java 8, Ruby (2.3.1)

### Build

To build the project:
```bash
    $ ./gradlew build
```

To ignore acceptanceTests
```bash
    $ ./gradlew build -x acceptanceTest
```

Remote apis available under https://pre-paird-card-dev.eu-west-1.elasticbeanstalk.com/cards/1/balance

### Endpoints example requests
The api provides a few different endpoints:

```
/payments
/cards
/earmarked
/merchants
/refunds
/withdraws
/reset
```

### /cards

#### Create card:
Make post request to `/cards`
```json
{
    "cardNumber": "5595301793561145",
    "cvv": "760",
    "firstName": "bob",
    "lastName": "john",
    "expireYear": 2020,
    "expireMonth": 2
}
```

#### Get card:
Make GET request to `/cards/1`

#### Get balance
Make GET request to `/cards/1/balance`

#### Create deposit
Make POST request to `/cards/1/deposit`
```json
{
    "cardId": 1,
    "amount": 100,
    "nonce": 0
}
```

#### Get transactions for card
Make GET to `cards/1/transactions?page=0&size=10&sort=amount,asc.`
This will return a list of transactions for the first page of results with at most 10 in current payload. It will also sort by the value of the transaction in ASC order.

The nonce value can be fetch by getting the balance and using that as the nonce. This must be done for every deposit.

Response:
```json
{
    "cardId": 1,
    "amount": 0,
    "nonce": 0
}
```

### /merchants

#### Create merchants
Make POST request to `/merchants`
```json
{
    "name": "amazon"
}
```

### /payments

#### Creating payments
Make POST request to `/payments`
```json
{
    "cardId": 1,
    "merchantId": 1,
    "amount": 10,
    "nonce": 1,
    "reason": "Coffee"
}
```
The nonce value can be fetched by getting the balance for a particular card you want to debit from and using that as the nonce. This must be done for every payment.

### /withdraws

#### Withdraw funds to merchant account
Make POST request to `/withdraws`
```json
{
    "merchantId": 1,
    "amount": 10,
    "nonce": 0
}
```
The nonce value can be fetched by getting the balance for a particular merchant you want to credit to and using that as the nonce. This must be done for every withdraw.
