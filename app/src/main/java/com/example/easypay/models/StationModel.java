package com.example.easypay.models;

import com.google.gson.annotations.SerializedName;

public class StationModel {

    @SerializedName("Station")
    private String station;

    public StationModel(String station) {
        this.station = station;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
