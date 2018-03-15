package com.curve.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Objects;

@Entity
@Table(name = "merchantBalance")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantBalance {

    @Id
    private Integer merchantId;
    private Double amount;
    @Version
    private int nonce;

    public MerchantBalance(){}

    private MerchantBalance(Builder builder) {
        merchantId = builder.merchantId;
        amount = builder.amount;
        nonce = builder.nonce;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(MerchantBalance copy) {
        Builder builder = new Builder();
        builder.merchantId = copy.getMerchantId();
        builder.amount = copy.getAmount();
        builder.nonce = copy.getNonce();
        return builder;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public Double getAmount() {
        return amount;
    }

    public int getNonce() {
        return nonce;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MerchantBalance that = (MerchantBalance) o;
        return nonce == that.nonce &&
                Objects.equals(merchantId, that.merchantId) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(merchantId, amount, nonce);
    }

    @Override
    public String toString() {
        return "MerchantBalance{" +
                "merchantId=" + merchantId +
                ", amount=" + amount +
                ", nonce=" + nonce +
                '}';
    }

    public static final class Builder {
        private Integer merchantId;
        private Double amount;
        private int nonce;

        private Builder() {
        }

        public Builder merchantId(Integer val) {
            merchantId = val;
            return this;
        }

        public Builder balance(Double val) {
            amount = val;
            return this;
        }

        public Builder nonce(int val) {
            nonce = val;
            return this;
        }

        public MerchantBalance build() {
            return new MerchantBalance(this);
        }
    }
}
