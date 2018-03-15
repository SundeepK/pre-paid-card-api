package com.curve.service;

import com.curve.domain.*;
import com.curve.exception.NotFoundException;
import com.curve.repository.MerchantBalanceRepository;
import com.curve.repository.MerchantRepository;
import com.curve.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Clock;

@Component
public class MerchantService {

    private final Clock clock;
    private final MerchantRepository merchantRepository;
    private final MerchantBalanceRepository merchantBalanceRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public MerchantService(MerchantRepository merchantRepository,
                           MerchantBalanceRepository merchantBalanceRepository,
                           TransactionRepository transactionRepository,
                           Clock clock) {
        this.merchantRepository = merchantRepository;
        this.merchantBalanceRepository = merchantBalanceRepository;
        this.transactionRepository = transactionRepository;
        this.clock = clock;
    }

    @Transactional
    public Merchant createMerchant(Merchant merchant){
        Merchant savedMerchant = merchantRepository.save(Merchant.newBuilder().name(merchant.getName()).build());
        merchantBalanceRepository.save(MerchantBalance.newBuilder().balance(0.0d).merchantId(savedMerchant.getMerchantId()).build());
        return savedMerchant;
    }

    public Merchant getMerchant(int merchantId) throws NotFoundException {
        Merchant merchant = merchantRepository.findByMerchantId(merchantId);
        if (merchant == null) {
            throw new NotFoundException("Merchant not found for id " + merchantId);
        }
        return merchant;
    }

    public MerchantBalance getBalance(int merchantId) throws NotFoundException {
        MerchantBalance merchantBalance = merchantBalanceRepository.findByMerchantId(merchantId);
        if (merchantBalance == null) {
            throw new NotFoundException("Merchant not found for id " + merchantId);
        }
        return merchantBalance;
    }

    @Transactional
    public Transaction updateBalance(Payment payment) throws NotFoundException {
        MerchantBalance existingBalance = getBalance(payment.getMerchantId());
        MerchantBalance balanceToSave = MerchantBalance.newBuilder(existingBalance)
                .balance(existingBalance.getAmount() + payment.getAmount())
                .nonce(payment.getNonce())
                .build();
        merchantBalanceRepository.save(balanceToSave);
        Transaction transactionToSave = Transaction.newBuilder()
                .merchantId(payment.getMerchantId())
                .amount(payment.getAmount())
                .date(clock.millis())
                .type(PaymentType.WITHDRAW.toString())
                .build();
        return transactionRepository.save(transactionToSave);
    }

}
