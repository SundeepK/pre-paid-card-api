package com.curve.repository;

import com.curve.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {

    public Card findByCardNumber(String cardNumber);

    public Card findByCardId(int cardId);

}
