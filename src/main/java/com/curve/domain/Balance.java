package com.curve.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Objects;

@Entity
@Table(name = "balance")
public class Balance {

    @Id
    private Integer cardId;
    private Double amount;
    @Version
    private Integer nonce;

    public Balance(){}

    public Balance(Integer cardId, Double amount, Integer nonce) {
        this.cardId = cardId;
        this.amount = amount;
        this.nonce = nonce;
    }

    public int getCardId() {
        return cardId;
    }

    public Double getAmount() {
        return amount;
    }

    public int getNonce() {
        return nonce;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "cardId=" + cardId +
                ", amount=" + amount +
                ", nonce=" + nonce +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Balance balance = (Balance) o;
        return Objects.equals(cardId, balance.cardId) &&
                Objects.equals(amount, balance.amount) &&
                Objects.equals(nonce, balance.nonce);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, amount, nonce);
    }
}
