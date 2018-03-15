package com.curve.service;

import com.curve.domain.*;
import com.curve.exception.CardAlreadyExistsException;
import com.curve.exception.InvalidRequestException;
import com.curve.exception.NotFoundException;
import com.curve.repository.BalanceRepository;
import com.curve.repository.CardRepository;
import com.curve.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.DateTimeException;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Fail.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private BalanceRepository balanceRepository;
    @Mock
    private TransactionRepository transactionRepository;

    private Clock clock = Clock.fixed(Instant.ofEpochMilli(1L), ZoneId.of("UTC"));

    private CardService underTest;

    @Before
    public void setUp() {
        underTest = new CardService(cardRepository, balanceRepository, transactionRepository, clock);
    }

    @Test
    public void testItCreatesCard() throws Exception {
        Card card = Card.newBuilder()
                .cardNumber("5595301793561145")
                .cvv("760")
                .firstName("s")
                .lastName("s")
                .expireYear(2020)
                .expireMonth(2)
                .build();

        when(cardRepository.save(card)).thenReturn(Card.newBuilder(card).cardId(1).build());

        underTest.createCard(card);

        verify(cardRepository).save(card);
        verify(balanceRepository).save(new Balance(1, 0.0d, 0));
    }

    @Test
    public void testItGetsCard() throws Exception {
        Card card = Card.newBuilder()
                .cardNumber("5595301793561145")
                .cardId(1)
                .cvv("760")
                .firstName("s")
                .lastName("s")
                .expireYear(2020)
                .expireMonth(2)
                .build();

        Card expected = Card.newBuilder(card)
                .cardNumber(null)
                .cardId(1)
                .cvv(null)
                .firstName("s")
                .lastName("s")
                .expireYear(null)
                .expireMonth(null)
                .build();

        when(cardRepository.findByCardId(1)).thenReturn(card);

        Card actualCard = underTest.getCard(1);

        assertEquals(expected, actualCard);
        verify(cardRepository).findByCardId(1);
    }

    @Test
    public void testItUpdatesBalance() throws Exception {
        Balance expected = new Balance(1, 10d, 0);
        when(balanceRepository.findByCardId(1)).thenReturn(expected);

        Transaction expectedTransaction = Transaction.newBuilder()
                .cardId(1)
                .amount(100d)
                .date(clock.millis())
                .type(PaymentType.DEPOSIT.toString())
                .build();

        Transaction actualTransaction = underTest.updateCardBalance(Payment.newBuilder().cardId(1).nonce(0).amount(100d).build(), PaymentType.DEPOSIT);

        assertEquals(expectedTransaction, actualTransaction);
        verify(balanceRepository).save(new Balance(1, 110d, 0));
        verify(transactionRepository).save(expectedTransaction);
    }

    @Test
    public void testItThrowsIfUpdatingBalanceToNegative() throws Exception {
        Balance expected = new Balance(1, 10d, 0);
        when(balanceRepository.findByCardId(1)).thenReturn(expected);

        try {
            underTest.updateCardBalance(Payment.newBuilder().cardId(1).nonce(0).amount(-100d).build(), PaymentType.DEPOSIT);
        } catch (InvalidRequestException e) {
            assertEquals("Update results in negative card balance.", e.getMessage());
        }
    }


    @Test
    public void testItThrowsIfNoCardFound() throws Exception {
        try {
            underTest.getCard(2);
            fail("NotFoundException not thrown.");
        } catch (NotFoundException e) {
            assertEquals("Card not found for card id 2", e.getMessage());
        }
    }

    @Test
    public void testItBalance() throws Exception {
        Balance expected = new Balance(1, 10d, 0);
        when(balanceRepository.findByCardId(1)).thenReturn(expected);

        Balance actualBalance = underTest.getBalance(1);

        assertEquals(expected, actualBalance);
    }

    @Test
    public void testItThrowsIfNoBalanceFound() throws Exception {
        try {
            underTest.getBalance(1);
            fail("NotFoundException not thrown.");
        } catch (NotFoundException e) {
            assertEquals("Card not found for card id 1", e.getMessage());
        }
    }


    @Test
    public void testItThrowsExceptionIfCardExists() throws Exception {
        Card card = Card.newBuilder()
                .cardNumber("5595301793561145")
                .cvv("760")
                .firstName("s")
                .lastName("s")
                .expireYear(2020)
                .expireMonth(2)
                .build();

        when(cardRepository.findByCardNumber("5595301793561145")).thenReturn(Card.newBuilder().cardId(1).build());

        try {
            underTest.createCard(card);
            fail("CardAlreadyExistsException not thrown.");
        } catch (CardAlreadyExistsException e) {
            assertEquals("Card already exists.", e.getMessage());
        }
    }

    @Test
    public void testItThrowsExceptionIfInvalidCardNumber() throws Exception {
        Card card = Card.newBuilder()
                .cardNumber("1")
                .cvv("760")
                .firstName("s")
                .lastName("s")
                .expireYear(2020)
                .expireMonth(2)
                .build();

        try {
            underTest.createCard(card);
            fail("InvalidRequestException not thrown.");
        } catch (InvalidRequestException e) {
            assertEquals("Invalid card number.", e.getMessage());
        }
    }

    @Test
    public void testItThrowsExceptionIfInvalidExpireYaar() throws Exception {
        Card card = Card.newBuilder()
                .cardNumber("5595301793561145")
                .cvv("760")
                .firstName("s")
                .lastName("s")
                .expireYear(2006)
                .expireMonth(2)
                .build();

        try {
            underTest.createCard(card);
            fail("InvalidRequestException not thrown.");
        } catch (InvalidRequestException e) {
            assertEquals("Card expired.", e.getMessage());
        }
    }

    @Test(expected = DateTimeException.class)
    public void testItThrowsExceptionIfInvalidExpireMonth() throws Exception {
        Card card = Card.newBuilder()
                .cardNumber("5595301793561145")
                .cvv("760")
                .firstName("s")
                .lastName("s")
                .expireYear(2026)
                .expireMonth(16)
                .build();
        underTest.createCard(card);
    }

    @Test
    public void testItThrowsExceptionIfFirstNameIsInvalid() throws Exception {
        Card card = Card.newBuilder()
                .cardNumber("5595301793561145")
                .cvv("760")
                .firstName("")
                .lastName("s")
                .expireYear(2026)
                .expireMonth(12)
                .build();
        try {
            underTest.createCard(card);
            fail("InvalidRequestException not thrown.");
        } catch (InvalidRequestException e) {
            assertEquals("Card full name invalid.", e.getMessage());
        }
    }

    @Test
    public void testItThrowsExceptionIfLastNameIsInvalid() throws Exception {
        Card card = Card.newBuilder()
                .cardNumber("5595301793561145")
                .cvv("760")
                .firstName("s")
                .lastName("")
                .expireYear(2026)
                .expireMonth(12)
                .build();
        try {
            underTest.createCard(card);
            fail("InvalidRequestException not thrown.");
        } catch (InvalidRequestException e) {
            assertEquals("Card full name invalid.", e.getMessage());
        }
    }

}