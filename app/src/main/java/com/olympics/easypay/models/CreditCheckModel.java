package com.olympics.easypay.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class CreditCheckModel {

    @SerializedName("card_number")
    private BigInteger cardNumber;

    @SerializedName("cvv_key")
    private int cvvKey;

    @SerializedName("expire_month")
    private int expireMonth;

    @SerializedName("expire_year")
    private int expireYear;

    @SerializedName("holder_name")
    private String holderName;

    public CreditCheckModel(BigInteger cardNumber, int cvvKey, int expireMonth, int expireYear, String holderName) {
        this.cardNumber = cardNumber;
        this.cvvKey = cvvKey;
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
        this.holderName = holderName;
    }

    public BigInteger getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(BigInteger cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCvvKey() {
        return cvvKey;
    }

    public void setCvvKey(int cvvKey) {
        this.cvvKey = cvvKey;
    }

    public int getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(int expireMonth) {
        this.expireMonth = expireMonth;
    }

    public int getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(int expireYear) {
        this.expireYear = expireYear;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }
}
