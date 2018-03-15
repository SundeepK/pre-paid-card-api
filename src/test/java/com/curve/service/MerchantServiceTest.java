package com.curve.service;

import com.curve.domain.MerchantBalance;
import com.curve.domain.Payment;
import com.curve.domain.PaymentType;
import com.curve.domain.Transaction;
import com.curve.exception.NotFoundException;
import com.curve.repository.MerchantBalanceRepository;
import com.curve.repository.MerchantRepository;
import com.curve.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MerchantServiceTest {

    @Mock
    private MerchantRepository merchantRepository;
    @Mock
    private MerchantBalanceRepository merchantBalanceRepository;
    @Mock
    private TransactionRepository transactionRepository;

    private Clock clock = Clock.fixed(Instant.ofEpochMilli(1L), ZoneId.of("UTC"));

    private MerchantService underTest;

    @Before
    public void setUp(){
        underTest = new MerchantService(merchantRepository, merchantBalanceRepository, transactionRepository, clock);
    }

    @Test
    public void testItUpdatesBalance() throws Exception {
        when(merchantBalanceRepository.findByMerchantId(2)).thenReturn(MerchantBalance.newBuilder().balance(0d).merchantId(2).build());
        Payment payment = Payment.newBuilder().amount(10d).nonce(1).merchantId(2).cardId(1).build();
        Transaction expectedTransaction = Transaction.newBuilder()
                .merchantId(2)
                .amount(10d)
                .date(clock.millis())
                .type(PaymentType.WITHDRAW.toString())
                .build();
        when(transactionRepository.save(expectedTransaction)).thenReturn(expectedTransaction);

        Transaction actualTransaction = underTest.updateBalance(payment);

        verify(merchantBalanceRepository).save(MerchantBalance.newBuilder().balance(10d).merchantId(2).nonce(1).build());
        assertEquals(expectedTransaction, actualTransaction);
    }

    @Test
    public void testItThrowsWhenNoMerchantFound(){
        try {
            underTest.getMerchant(1);
            fail("NotFoundException not thrown.");
        } catch (NotFoundException e) {
            assertEquals("Merchant not found for id " + 1, e.getMessage());
        }
    }

    @Test
    public void testItThrowsWhenNoMerchantFoundForCheckingBalance(){
        try {
            underTest.getBalance(1);
            fail("NotFoundException not thrown.");
        } catch (NotFoundException e) {
            assertEquals("Merchant not found for id " + 1, e.getMessage());
        }
    }

    @Test
    public void testItGetsBalance() throws Exception {
        MerchantBalance expectedBalance = MerchantBalance.newBuilder().balance(0d).merchantId(2).build();
        when(merchantBalanceRepository.findByMerchantId(2)).thenReturn(expectedBalance);

        MerchantBalance actualBalance = underTest.getBalance(2);

        assertEquals(expectedBalance, actualBalance);
    }

}