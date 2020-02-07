package com.example.easypay.models;

import com.google.gson.annotations.SerializedName;

public class TrainTicketModel {
    @SerializedName("start_Station")
    private String startStation;

    @SerializedName("End_Station")
    private String endStation;

    @SerializedName("ticket_time")
    private String ticketTime;

    @SerializedName("reserve_time")
    private String reserveTime;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("cost")
    private int cost;

    @SerializedName("chair_number")
    private int chairNumber;

    public TrainTicketModel(String startStation, String endStation, String ticketTime, String reserveTime, int quantity, int cost, int chairNumber) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.ticketTime = ticketTime;
        this.reserveTime = reserveTime;
        this.quantity = quantity;
        this.cost = cost;
        this.chairNumber = chairNumber;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getChairNumber() {
        return chairNumber;
    }

    public void setChairNumber(int chairNumber) {
        this.chairNumber = chairNumber;
    }
}
