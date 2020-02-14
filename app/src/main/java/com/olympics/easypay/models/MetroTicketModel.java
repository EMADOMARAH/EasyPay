package com.olympics.easypay.models;

import com.google.gson.annotations.SerializedName;

public class MetroTicketModel {
    @SerializedName("Ticket_number")
    private String ticketNumber;

    @SerializedName("start_Station")
    private String startStation;

    @SerializedName("End_Station")
    private String endStation;

    @SerializedName("ticket_date")
    private String ticketDate;

    @SerializedName("Metro_cost")
    private String metroCost;

    public MetroTicketModel(String ticketNumber, String startStation, String endStation, String ticketDate, String metroCost) {
        this.ticketNumber = ticketNumber;
        this.startStation = startStation;
        this.endStation = endStation;
        this.ticketDate = ticketDate;
        this.metroCost = metroCost;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
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

    public String getMetroCost() {
        return metroCost;
    }

    public void setMetroCost(String metroCost) {
        this.metroCost = metroCost;
    }
}
