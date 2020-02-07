package com.example.easypay.models;

import com.google.gson.annotations.SerializedName;

public class ChargeHistoryModel {
    @SerializedName("charge_amount")
    private int chargeAmount;

    @SerializedName("charge_date")
    private String chargeDate;

    @SerializedName("charge_method")
    private String chargeMethod;

    public ChargeHistoryModel(int chargeAmount, String chargeDate, String chargeMethod) {
        this.chargeAmount = chargeAmount;
        this.chargeDate = chargeDate;
        this.chargeMethod = chargeMethod;
    }

    public int getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(int chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(String chargeDate) {
        this.chargeDate = chargeDate;
    }

    public String getChargeMethod() {
        return chargeMethod;
    }

    public void setChargeMethod(String chargeMethod) {
        this.chargeMethod = chargeMethod;
    }
}
