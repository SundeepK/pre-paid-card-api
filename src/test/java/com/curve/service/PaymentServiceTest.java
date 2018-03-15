package com.curve.service;

import com.curve.domain.MerchantBalance;
import com.curve.domain.Payment;
import com.curve.domain.PaymentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    @Mock
    private MerchantService merchantService;
    @Mock
    private EarMarkedService earMarkedService;
    @Mock
    private CardService cardService;

    private PaymentService underTest;


    @Before
    public void setUp() throws Exception {
        when(merchantService.getBalance(1)).thenReturn(new MerchantBalance());
        underTest = new PaymentService(merchantService, earMarkedService, cardService);
    }

    @Test
    public void testItAuthorizesPayment() throws Exception {
        Payment payment = Payment.newBuilder()
                .amount(100d)
                .cardId(1)
                .merchantId(1)
                .nonce(0)
                .build();

        underTest.authorizePayment(payment);

        verify(merchantService).getBalance(1);
        verify(cardService).updateCardBalance(Payment.newBuilder().cardId(1).merchantId(1).amount(-100d).nonce(0).build(), PaymentType.PAYMENT);
        verify(earMarkedService).updateEarMarked(payment);

    }

}