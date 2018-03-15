package com.curve.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment {

    private Integer cardId;
    private Integer merchantId;
    private Double amount;
    private int nonce;
    private String reason;

    Payment() {}

    public Payment(int cardId, int merchantId, double amount, int nonce, String reason) {
        this.cardId = cardId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.nonce = nonce;
        this.reason = reason;
    }

    private Payment(Builder builder) {
        cardId = builder.cardId;
        merchantId = builder.merchantId;
        amount = builder.amount;
        nonce = builder.nonce;
        reason = builder.reason;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Payment copy) {
        Builder builder = new Builder();
        builder.cardId = copy.getCardId();
        builder.merchantId = copy.getMerchantId();
        builder.amount = copy.getAmount();
        builder.nonce = copy.getNonce();
        builder.reason = copy.getReason();
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

    public int getNonce() {
        return nonce;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return cardId == payment.cardId &&
                merchantId == payment.merchantId &&
                Double.compare(payment.amount, amount) == 0 &&
                nonce == payment.nonce &&
                Objects.equals(reason, payment.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, merchantId, amount, nonce, reason);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "cardId=" + cardId +
                ", merchantId=" + merchantId +
                ", amount=" + amount +
                ", nonce=" + nonce +
                ", reason='" + reason + '\'' +
                '}';
    }

    public static final class Builder {
        private Integer cardId;
        private Integer merchantId;
        private Double amount;
        private int nonce;
        private String reason;

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

        public Builder nonce(int val) {
            nonce = val;
            return this;
        }

        public Builder reason(String val) {
            reason = val;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }
}
