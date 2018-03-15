package com.curve.service;

import com.curve.domain.EarMarked;
import com.curve.domain.Payment;
import com.curve.domain.PaymentType;
import com.curve.domain.Transaction;
import com.curve.exception.InvalidRequestException;
import com.curve.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class PaymentService {

    private MerchantService merchantService;
    private EarMarkedService earMarkedService;
    private CardService cardService;

    @Autowired
    public PaymentService(MerchantService merchantService,
                          EarMarkedService earMarkedService,
                          CardService cardService) {
        this.merchantService = merchantService;
        this.earMarkedService = earMarkedService;
        this.cardService = cardService;
    }

    public Transaction authorizePayment(Payment payment) throws NotFoundException, InvalidRequestException {
        merchantService.getBalance(payment.getMerchantId());
        Payment paymentToProcess = Payment.newBuilder(payment).amount(-payment.getAmount()).build();
        Transaction transaction = cardService.updateCardBalance(paymentToProcess, PaymentType.PAYMENT);
        earMarkedService.updateEarMarked(payment);
        return transaction;
    }

    public Transaction refund(Payment payment) throws NotFoundException, InvalidRequestException {
        earMarkedService.updateEarMarked(getDebitPayment(payment));
        return cardService.updateCardBalance(payment, PaymentType.REFUND);
    }

    public Transaction withdraw(Payment payment) throws InvalidRequestException, NotFoundException {
        EarMarked earMarkedBalance = earMarkedService.getEarMarkedBalance(payment.getCardId(), payment.getMerchantId());
        if (earMarkedBalance == null) {
            throw new NotFoundException("No balance found for cardId " + payment.getCardId() + " merchantId " + payment.getMerchantId());
        }
        earMarkedService.updateEarMarked(getDebitPayment(payment));
        return merchantService.updateBalance(payment);
    }

    private Payment getDebitPayment(Payment payment) {
        return Payment.newBuilder(payment).amount(-payment.getAmount()).build();
    }

}
