package com.example.easypay.models;

import com.google.gson.annotations.SerializedName;

public class AvailableTrainsModel {
    @SerializedName("Avilable_Trains")
    private String availableTrains;

    @SerializedName("Time")
    private String time;

    public AvailableTrainsModel(String availableTrains, String time) {
        this.availableTrains = availableTrains;
        this.time = time;
    }

    public String getAvailableTrains() {
        return availableTrains;
    }

    public void setAvailableTrains(String availableTrains) {
        this.availableTrains = availableTrains;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
