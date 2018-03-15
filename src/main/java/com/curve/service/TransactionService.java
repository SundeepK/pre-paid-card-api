package com.curve.service;

import com.curve.domain.Transaction;
import com.curve.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class TransactionService {

    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Page<Transaction> getTransactionsByCardId(int cardId, Pageable pageable){
        return transactionRepository.findTransactionsByCardId(cardId, pageable);
    }

}
