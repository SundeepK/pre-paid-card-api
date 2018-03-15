package com.curve.repository;

import com.curve.domain.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, String> {

    public Balance findByCardId(int cardId);

}
