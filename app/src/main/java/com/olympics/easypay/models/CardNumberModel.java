package com.olympics.easypay.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class CardNumberModel {
    @SerializedName("card_number")
    private BigInteger cardNumber;

    public CardNumberModel(BigInteger cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigInteger getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(BigInteger cardNumber) {
        this.cardNumber = cardNumber;
    }
}
