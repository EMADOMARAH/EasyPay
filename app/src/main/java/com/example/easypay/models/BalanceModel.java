package com.example.easypay.models;

import com.google.gson.annotations.SerializedName;

public class BalanceModel {

    @SerializedName("last_charge")
    private String lastCharge;

    @SerializedName("current_balance")
    private int currentBalance;

    public BalanceModel(String lastCharge, int currentBalance) {
        this.lastCharge = lastCharge;
        this.currentBalance = currentBalance;
    }

    public String getLastCharge() {
        return lastCharge;
    }

    public void setLastCharge(String lastCharge) {
        this.lastCharge = lastCharge;
    }

    public int getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(int currentBalance) {
        this.currentBalance = currentBalance;
    }
}
