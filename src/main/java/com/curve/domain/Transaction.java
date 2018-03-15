package com.curve.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "transaction")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;
    private Integer cardId;
    private Integer merchantId;
    private String reason;
    private String type;
    private Double amount;
    private Long epochMillis;

    public Transaction(){}

    private Transaction(Builder builder) {
        transactionId = builder.transactionId;
        cardId = builder.cardId;
        merchantId = builder.merchantId;
        reason = builder.reason;
        type = builder.type;
        amount = builder.amount;
        epochMillis = builder.date;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Transaction copy) {
        Builder builder = new Builder();
        builder.transactionId = copy.getTransactionId();
        builder.cardId = copy.getCardId();
        builder.merchantId = copy.getMerchantId();
        builder.reason = copy.getReason();
        builder.type = copy.getType();
        builder.amount = copy.getAmount();
        builder.date = copy.getEpochMillis();
        return builder;
    }


    public Long getEpochMillis() {
        return epochMillis;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public String getReason() {
        return reason;
    }

    public String getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(cardId, that.cardId) &&
                Objects.equals(merchantId, that.merchantId) &&
                Objects.equals(reason, that.reason) &&
                Objects.equals(type, that.type) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(epochMillis, that.epochMillis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, cardId, merchantId, reason, type, amount, epochMillis);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", cardId=" + cardId +
                ", merchantId=" + merchantId +
                ", reason='" + reason + '\'' +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", epochMillis=" + epochMillis +
                '}';
    }

    public static final class Builder {
        private Integer transactionId;
        private Integer cardId;
        private Integer merchantId;
        private String reason;
        private String type;
        private Double amount;
        private Long date;

        private Builder() {
        }

        public Builder transactionId(Integer val) {
            transactionId = val;
            return this;
        }

        public Builder cardId(Integer val) {
            cardId = val;
            return this;
        }

        public Builder merchantId(Integer val) {
            merchantId = val;
            return this;
        }


        public Builder reason(String val) {
            reason = val;
            return this;
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder amount(Double val) {
            amount = val;
            return this;
        }

        public Builder date(Long val) {
            date = val;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
