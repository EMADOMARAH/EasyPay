package com.example.easypay.models;

import com.google.gson.annotations.SerializedName;

public class WalletModel {
    @SerializedName("charge_Date")
    private String chargeDate;

    @SerializedName("Last_Charge")
    private int lastCharge;

    @SerializedName("current_balance")
    private int currentBalance;

    public WalletModel(String chargeDate, int lastCharge, int currentBalance) {
        this.chargeDate = chargeDate;
        this.lastCharge = lastCharge;
        this.currentBalance = currentBalance;
    }

    public String getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(String chargeDate) {
        this.chargeDate = chargeDate;
    }

    public int getLastCharge() {
        return lastCharge;
    }

    public void setLastCharge(int lastCharge) {
        this.lastCharge = lastCharge;
    }

    public int getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(int currentBalance) {
        this.currentBalance = currentBalance;
    }
}
