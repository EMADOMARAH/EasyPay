package com.olympics.easypay.models;

import com.google.gson.annotations.SerializedName;

public class TrainHistoryModel {
    @SerializedName("ticket_number")
    private String ticketNumber;

    @SerializedName("Start_Station")
    private String startStation;

    @SerializedName("End_Station")
    private String endStation;

    @SerializedName("reserve_time")
    private String reserveTime;

    public TrainHistoryModel(String ticketNumber, String startStation, String endStation, String reserveTime) {
        this.ticketNumber = ticketNumber;
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

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
}
