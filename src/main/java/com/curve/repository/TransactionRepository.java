package com.curve.repository;

import com.curve.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    public Page<Transaction> findTransactionsByCardId(int cardId, Pageable pageable);


}
