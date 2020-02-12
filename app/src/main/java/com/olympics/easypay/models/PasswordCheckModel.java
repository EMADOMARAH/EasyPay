package com.olympics.easypay.models;

import com.google.gson.annotations.SerializedName;

public class PasswordCheckModel {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public PasswordCheckModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
