package com.olympics.easypay.models;

import com.google.gson.annotations.SerializedName;

public class PhoneCheckModel {
    @SerializedName("sign up")
    private String result;

    public PhoneCheckModel(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
