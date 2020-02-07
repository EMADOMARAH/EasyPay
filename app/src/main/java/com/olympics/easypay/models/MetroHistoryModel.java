package com.olympics.easypay.models;

import com.google.gson.annotations.SerializedName;

public class MetroHistoryModel {
    @SerializedName("Start_Station")
    private String startStation;

    @SerializedName("End_Station")
    private String endStation;

    @SerializedName("ticket_date")
    private String ticketDate;

    public MetroHistoryModel(String startStation, String endStation, String ticketDate) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.ticketDate = ticketDate;
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

    public String getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(String ticketDate) {
        this.ticketDate = ticketDate;
    }
}
