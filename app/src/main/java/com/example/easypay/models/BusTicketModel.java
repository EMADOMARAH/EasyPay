package com.example.easypay.models;

import com.google.gson.annotations.SerializedName;

public class BusTicketModel {
    @SerializedName("ticket_number")
    private int ticketNumber;

    @SerializedName("line_number")
    private int lineNumber;

    @SerializedName("ticket_date")
    private String ticketDate;

    @SerializedName("line_cost")
    private int lineCost;

    public BusTicketModel(int ticketNumber, int lineNumber, String ticketDate, int lineCost) {
        this.ticketNumber = ticketNumber;
        this.lineNumber = lineNumber;
        this.ticketDate = ticketDate;
        this.lineCost = lineCost;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(String ticketDate) {
        this.ticketDate = ticketDate;
    }

    public int getLineCost() {
        return lineCost;
    }

    public void setLineCost(int lineCost) {
        this.lineCost = lineCost;
    }
}
