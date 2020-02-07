package com.olympics.easypay.models;

import com.google.gson.annotations.SerializedName;

public class SetSettingModel {

    @SerializedName("update")
    private String update;

    public SetSettingModel(String update) {
        this.update = update;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}
