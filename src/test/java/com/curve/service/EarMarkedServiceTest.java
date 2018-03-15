package com.curve.service;

import com.curve.domain.EarMarked;
import com.curve.domain.Payment;
import com.curve.exception.InvalidRequestException;
import com.curve.repository.EarMarkedRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EarMarkedServiceTest {

    @Mock
    private EarMarkedRepository earMarkedRepository;

    private EarMarkedService underTest;


    @Before
    public void setUp(){
        underTest = new EarMarkedService(earMarkedRepository);
    }

    @Test
    public void testItEarMarksFunds() throws Exception {
        Payment payment = Payment.newBuilder()
                .amount(-100d)
                .cardId(1)
                .merchantId(2)
                .nonce(0)
                .build();
        EarMarked expectedEarMarked = EarMarked.newBuilder()
                .cardId(1)
                .merchantId(2)
                .amount(200d)
                .build();
        when(earMarkedRepository.save(expectedEarMarked)).thenReturn(expectedEarMarked);
        when(earMarkedRepository.findByCardIdAndMerchantId(1, 2)).thenReturn(EarMarked.newBuilder().amount(300d).cardId(1).merchantId(2).build());

        EarMarked actualEarMarked = underTest.updateEarMarked(payment);

        assertEquals(expectedEarMarked, actualEarMarked);
    }

    @Test
    public void testItEarMarksFundsCanBeMadeZero() throws Exception {
        Payment payment = Payment.newBuilder()
                .amount(-100d)
                .cardId(1)
                .merchantId(2)
                .nonce(0)
                .build();
        EarMarked expectedEarMarked = EarMarked.newBuilder()
                .cardId(1)
                .merchantId(2)
                .amount(0d)
                .build();
        when(earMarkedRepository.save(expectedEarMarked)).thenReturn(expectedEarMarked);
        when(earMarkedRepository.findByCardIdAndMerchantId(1, 2)).thenReturn(EarMarked.newBuilder().amount(100d).cardId(1).merchantId(2).build());

        EarMarked actualEarMarked = underTest.updateEarMarked(payment);

        assertEquals(expectedEarMarked, actualEarMarked);
    }

    @Test
    public void testItThrowsIfUpdateResultsInNegativeBalance() throws Exception {
        Payment payment = Payment.newBuilder()
                .amount(-100d)
                .cardId(1)
                .merchantId(2)
                .nonce(0)
                .build();

        when(earMarkedRepository.findByCardIdAndMerchantId(1, 2)).thenReturn(EarMarked.newBuilder().amount(10d).cardId(1).merchantId(2).build());

        try {
            underTest.updateEarMarked(payment);
            fail("InvalidRequestException not thrown.");
        } catch (InvalidRequestException e) {
            assertEquals("Payment results in negative ear mark balance.", e.getMessage());
        }
    }
}