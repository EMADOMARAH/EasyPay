package com.example.easypay.models;

import com.google.gson.annotations.SerializedName;

public class TrainHistoryModel {
    @SerializedName("Start_Station")
    private String startStation;

    @SerializedName("End_Station")
    private String endStation;

    @SerializedName("reserve_time")
    private String reserveTime;

    public TrainHistoryModel(String startStation, String endStation, String reserveTime) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.reserveTime = reserveTime;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }
}
