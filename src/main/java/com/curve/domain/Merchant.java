package com.curve.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "merchant")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer merchantId;
    private String name;

    public Merchant(){}

    private Merchant(Builder builder) {
        merchantId = builder.merchantId;
        name = builder.name;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Merchant copy) {
        Builder builder = new Builder();
        builder.merchantId = copy.getMerchantId();
        builder.name = copy.getName();
        return builder;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Merchant merchant = (Merchant) o;
        return Objects.equals(merchantId, merchant.merchantId) &&
                Objects.equals(name, merchant.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(merchantId, name);
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "merchantId=" + merchantId +
                ", name='" + name + '\'' +
                '}';
    }

    public static final class Builder {
        private Integer merchantId;
        private String name;

        private Builder() {
        }

        public Builder merchantId(Integer val) {
            merchantId = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Merchant build() {
            return new Merchant(this);
        }
    }
}
