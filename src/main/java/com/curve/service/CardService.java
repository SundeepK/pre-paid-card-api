package com.curve.service;

import com.curve.domain.*;
import com.curve.exception.CardAlreadyExistsException;
import com.curve.exception.InvalidRequestException;
import com.curve.exception.NotFoundException;
import com.curve.repository.BalanceRepository;
import com.curve.repository.CardRepository;
import com.curve.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.fatehi.creditcardnumber.AccountNumber;
import us.fatehi.creditcardnumber.ExpirationDate;
import us.fatehi.creditcardnumber.Name;

import javax.transaction.Transactional;
import java.time.Clock;

@Component
public class CardService {

    private final Clock clock;
    private CardRepository cardRepository;
    private BalanceRepository balanceRepository;
    private TransactionRepository transactionRepository;

    @Autowired
    public CardService(CardRepository cardRepository,
                       BalanceRepository balanceRepository,
                       TransactionRepository transactionRepository,
                       Clock clock) {
        this.cardRepository = cardRepository;
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.clock = clock;
    }

    @Transactional
    public Card createCard(Card card) throws CardAlreadyExistsException, InvalidRequestException {
        assertCard(card);
        Card existingCard = cardRepository.findByCardNumber(card.getCardNumber());
        if (existingCard != null) {
            throw new CardAlreadyExistsException("Card already exists.");
        }
        Card savedCard = Card.newBuilder(cardRepository.save(card))
                .cardNumber(null)
                .cvv(null)
                .expireYear(null)
                .expireMonth(null)
                .build();
        balanceRepository.save(new Balance(savedCard.getCardId(), 0.0d, 0));
        return savedCard;
    }

    public Card getCard(int id) throws NotFoundException {
        Card card = cardRepository.findByCardId(id);
        if (card == null) {
            throw new NotFoundException("Card not found for card id " + id);
        }
        return Card.newBuilder()
                .cardId(card.getCardId())
                .firstName(card.getFirstName())
                .lastName(card.getFirstName())
                .cardNumber(null)
                .cvv(null)
                .expireYear(null)
                .expireMonth(null)
                .build();
    }

    public Balance getBalance(int cardId) throws NotFoundException {
        Balance existingBal = balanceRepository.findByCardId(cardId);
        if (existingBal == null) {
            throw new NotFoundException("Card not found for card id " + cardId);
        }
        return existingBal;
    }

    @Transactional
    public Transaction updateCardBalance(Payment payment, PaymentType type) throws NotFoundException, InvalidRequestException {
       return updateBalance(payment, type);
    }

    private Transaction updateBalance(Payment payment, PaymentType type) throws NotFoundException, InvalidRequestException {
        Balance existingBal = balanceRepository.findByCardId(payment.getCardId());
        if (existingBal == null) {
            throw new NotFoundException("Card not found for card id " + payment.getCardId());
        }
        Balance newBalance = new Balance(existingBal.getCardId(), payment.getAmount() + existingBal.getAmount(), payment.getNonce());
        if (newBalance.getAmount() < 0) {
            throw new InvalidRequestException("Update results in negative card balance.");
        }
        Transaction transactionToSave = getTransaction(payment, type);
        transactionRepository.save(transactionToSave);
        balanceRepository.save(newBalance);
        return transactionToSave;
    }

    private Transaction getTransaction(Payment payment, PaymentType type) {
        return Transaction.newBuilder()
                .cardId(payment.getCardId())
                .merchantId(payment.getMerchantId())
                .amount(Math.abs(payment.getAmount()))
                .type(type.toString())
                .date(clock.millis())
                .reason(payment.getReason())
                .build();
    }

    private void assertCard(Card card) throws InvalidRequestException {
        AccountNumber accountNumber = new AccountNumber(card.getCardNumber());
        if (!accountNumber.passesLuhnCheck() || !accountNumber.isPrimaryAccountNumberValid()) {
            throw new InvalidRequestException("Invalid card number.");
        }
        ExpirationDate expiration = new ExpirationDate(card.getExpireYear(), card.getExpireMonth());
        if (expiration.isExpired()) {
            throw new InvalidRequestException("Card expired.");
        }
        Name name = new Name(card.getFirstName(), card.getLastName());
        if(!name.hasFullName()) {
            throw new InvalidRequestException("Card full name invalid.");
        }
    }
}
