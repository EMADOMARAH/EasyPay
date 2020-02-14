package com.olympics.easypay.models;

import com.google.gson.annotations.SerializedName;

public class BusTicketModel {
    @SerializedName("ticket_number")
    private String ticketNumber;
    @SerializedName("line_number")
    private String lineNumber;
    @SerializedName("ticket_date")
    private String ticketDate;
    @SerializedName("line_cost")
    private String lineCost;

    public BusTicketModel(String ticketNumber, String lineNumber, String ticketDate, String lineCost) {
        this.ticketNumber = ticketNumber;
        this.lineNumber = lineNumber;
        this.ticketDate = ticketDate;
        this.lineCost = lineCost;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(String ticketDate) {
        this.ticketDate = ticketDate;
    }

    public String getLineCost() {
        return lineCost;
    }

    public void setLineCost(String lineCost) {
        this.lineCost = lineCost;
    }
}
