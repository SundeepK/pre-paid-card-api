package com.curve.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "card")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cardId;
    @Column(unique=true)
    private String cardNumber;
    private String type;
    private String cvv;
    private String firstName;
    private String lastName;
    private Integer expireYear;
    private Integer expireMonth;

    public Card(){}

    private Card(Builder builder) {
        cardId = builder.cardId;
        cardNumber = builder.cardNumber;
        type = builder.type;
        cvv = builder.cvv;
        firstName = builder.firstName;
        lastName = builder.lastName;
        expireYear = builder.expireYear;
        expireMonth = builder.expireMonth;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Card copy) {
        Builder builder = new Builder();
        builder.cardId = copy.getCardId();
        builder.cardNumber = copy.getCardNumber();
        builder.type = copy.getType();
        builder.cvv = copy.getCvv();
        builder.firstName = copy.getFirstName();
        builder.lastName = copy.getLastName();
        builder.expireYear = copy.getExpireYear();
        builder.expireMonth = copy.getExpireMonth();
        return builder;
    }

    public int getCardId() {
        return cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getType() {
        return type;
    }

    public String getCvv() {
        return cvv;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getExpireYear() {
        return expireYear;
    }

    public Integer getExpireMonth() {
        return expireMonth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return cardId == card.cardId &&
                expireYear == card.expireYear &&
                expireMonth == card.expireMonth &&
                Objects.equals(cardNumber, card.cardNumber) &&
                Objects.equals(type, card.type) &&
                Objects.equals(cvv, card.cvv) &&
                Objects.equals(firstName, card.firstName) &&
                Objects.equals(lastName, card.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, cardNumber, type, cvv, firstName, lastName, expireYear, expireMonth);
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardId=" + cardId +
                ", cardNumber='" + cardNumber + '\'' +
                ", type='" + type + '\'' +
                ", cvv='" + cvv + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", expireYear=" + expireYear +
                ", expireMonth=" + expireMonth +
                '}';
    }


    public static final class Builder {
        private int cardId;
        private String cardNumber;
        private String type;
        private String cvv;
        private String firstName;
        private String lastName;
        private Integer expireYear;
        private Integer expireMonth;

        private Builder() {
        }

        public Builder cardId(int val) {
            cardId = val;
            return this;
        }

        public Builder cardNumber(String val) {
            cardNumber = val;
            return this;
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder cvv(String val) {
            cvv = val;
            return this;
        }

        public Builder firstName(String val) {
            firstName = val;
            return this;
        }

        public Builder lastName(String val) {
            lastName = val;
            return this;
        }

        public Builder expireYear(Integer val) {
            expireYear = val;
            return this;
        }

        public Builder expireMonth(Integer val) {
            expireMonth = val;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }
}
