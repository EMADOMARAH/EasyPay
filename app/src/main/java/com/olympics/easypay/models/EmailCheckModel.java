package com.olympics.easypay.models;

import com.google.gson.annotations.SerializedName;

public class EmailCheckModel {
    @SerializedName("email")
    private String email;

    public EmailCheckModel(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
