package com.example.easypay.models;

import com.google.gson.annotations.SerializedName;

public class TokenModel {

    @SerializedName("id")
    private String id;

    public TokenModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
