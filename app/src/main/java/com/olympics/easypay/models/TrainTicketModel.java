package com.olympics.easypay.models;

import com.google.gson.annotations.SerializedName;

public class TrainTicketModel {
    @SerializedName("Start_Station")
    private String startStation;

    @SerializedName("End_Station")
    private String endStation;

    @SerializedName("ticket_time")
    private String ticketTime;

    @SerializedName("reserve_time")
    private String reserveTime;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("cost")
    private String cost;

    @SerializedName("chair_number")
    private String chairNumber;

    @SerializedName("line_id")
    private String lineId;

    public TrainTicketModel(String startStation, String endStation, String ticketTime, String reserveTime, String quantity, String cost, String chairNumber, String lineId) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.ticketTime = ticketTime;
        this.reserveTime = reserveTime;
        this.quantity = quantity;
        this.cost = cost;
        this.chairNumber = chairNumber;
        this.lineId = lineId;
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

    public String getTicketTime() {
        return ticketTime;
    }

    public void setTicketTime(String ticketTime) {
        this.ticketTime = ticketTime;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getChairNumber() {
        return chairNumber;
    }

    public void setChairNumber(String chairNumber) {
        this.chairNumber = chairNumber;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }
}
