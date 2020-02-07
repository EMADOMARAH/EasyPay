package com.example.easypay.models;

import com.google.gson.annotations.SerializedName;

public class LineModel {
    @SerializedName("line_name")
    private String lineName;

    public LineModel(String lineName) {
        this.lineName = lineName;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }
}
