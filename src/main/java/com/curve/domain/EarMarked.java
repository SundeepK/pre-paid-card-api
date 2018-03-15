package com.curve.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "earMarked")
@IdClass(EarMarkedId.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EarMarked {

    @Id
    private Integer cardId;

    @Id
    private Integer merchantId;
    private Double amount;

    public EarMarked(){}

    private EarMarked(Builder builder) {
        cardId = builder.cardId;
        merchantId = builder.merchantId;
        amount = builder.amount;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(EarMarked copy) {
        Builder builder = new Builder();
        builder.cardId = copy.getCardId();
        builder.merchantId = copy.getMerchantId();
        builder.amount = copy.getAmount();
        return builder;
    }


    public Integer getCardId() {
        return cardId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public Double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EarMarked earMarked = (EarMarked) o;
        return Objects.equals(cardId, earMarked.cardId) &&
                Objects.equals(merchantId, earMarked.merchantId) &&
                Objects.equals(amount, earMarked.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, merchantId, amount);
    }

    @Override
    public String toString() {
        return "EarMarked{" +
                "cardId=" + cardId +
                ", merchantId=" + merchantId +
                ", amount=" + amount +
                '}';
    }

    public static final class Builder {
        private Integer cardId;
        private Integer merchantId;
        private Double amount;

        private Builder() {
        }

        public Builder cardId(Integer val) {
            cardId = val;
            return this;
        }

        public Builder merchantId(Integer val) {
            merchantId = val;
            return this;
        }

        public Builder amount(Double val) {
            amount = val;
            return this;
        }

        public EarMarked build() {
            return new EarMarked(this);
        }
    }
}
