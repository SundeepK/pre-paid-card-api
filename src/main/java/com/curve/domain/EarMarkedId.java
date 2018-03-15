package com.curve.domain;

import java.io.Serializable;
import java.util.Objects;

public class EarMarkedId implements Serializable {

    private Integer cardId;
    private Integer merchantId;

    public EarMarkedId(){}

    public EarMarkedId(Integer cardId, Integer merchantId) {
        this.cardId = cardId;
        this.merchantId = merchantId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EarMarkedId that = (EarMarkedId) o;
        return Objects.equals(cardId, that.cardId) &&
                Objects.equals(merchantId, that.merchantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, merchantId);
    }

    @Override
    public String toString() {
        return "EarMarkedId{" +
                "cardId=" + cardId +
                ", merchantId=" + merchantId +
                '}';
    }
}
