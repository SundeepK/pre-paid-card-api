package com.curve.service;

import com.curve.domain.EarMarked;
import com.curve.domain.Payment;
import com.curve.exception.InvalidRequestException;
import com.curve.exception.NotFoundException;
import com.curve.repository.EarMarkedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class EarMarkedService {

    private EarMarkedRepository earMarkedRepository;

    @Autowired
    public EarMarkedService(EarMarkedRepository earMarkedRepository) {
        this.earMarkedRepository = earMarkedRepository;
    }

    public EarMarked getEarMarkedBalance(int cardId, int merchantId) throws NotFoundException {
        EarMarked earMarkedService = earMarkedRepository.findByCardIdAndMerchantId(cardId, merchantId);
        if (earMarkedService == null) {
            throw new NotFoundException("Ear marked balance not found for cardId " + cardId + " merchant id " + merchantId);
        }
        return earMarkedService;
    }

    public EarMarked getTotalEarMarkedBalance(int merchantId){
        double sum = earMarkedRepository.sumBydMerchantId(merchantId);
        return EarMarked.newBuilder().amount(sum).merchantId(merchantId).build();
    }

    @Transactional
    public EarMarked updateEarMarked(Payment payment) throws InvalidRequestException {
        EarMarked existingEarMarked = earMarkedRepository.findByCardIdAndMerchantId(payment.getCardId(), payment.getMerchantId());
        double amount = payment.getAmount();
        if (existingEarMarked != null) {
            amount = existingEarMarked.getAmount() + amount;
        }
        if (amount < 0) {
            throw  new InvalidRequestException("Payment results in negative ear mark balance.");
        }
        EarMarked newEarMarked = EarMarked.newBuilder()
                .cardId(payment.getCardId())
                .merchantId(payment.getMerchantId())
                .amount(amount)
                .build();
        return earMarkedRepository.save(newEarMarked);
    }

}
