package com.example.easypay.models;

import com.google.gson.annotations.SerializedName;

public class BusHistoryModel {
    @SerializedName("ticket_number")
    private int ticketNumber;

    @SerializedName("ticket_date")
    private String ticketDate;

    public BusHistoryModel(int ticketNumber, String ticketDate) {
        this.ticketNumber = ticketNumber;
        this.ticketDate = ticketDate;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(String ticketDate) {
        this.ticketDate = ticketDate;
    }
}
