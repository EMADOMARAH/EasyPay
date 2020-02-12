package com.olympics.easypay.models;

import com.google.gson.annotations.SerializedName;

public class RetrievePasswordModel {

    @SerializedName("id")
    private String id;

    public RetrievePasswordModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
