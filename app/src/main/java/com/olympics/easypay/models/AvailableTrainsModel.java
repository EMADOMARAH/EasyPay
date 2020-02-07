package com.olympics.easypay.models;

import com.google.gson.annotations.SerializedName;

public class AvailableTrainsModel {
    @SerializedName("Train_number")
    private String trainNumber;

    @SerializedName("Time")
    private String time;

    public AvailableTrainsModel(String trainNumber, String time) {
        this.trainNumber = trainNumber;
        this.time = time;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
