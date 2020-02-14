package com.olympics.easypay.models;

import com.google.gson.annotations.SerializedName;

public class BusHistoryModel {
    @SerializedName("ticket_number")
    private String ticketNumber;

    @SerializedName("ticket_date")
    private String ticketDate;

    public BusHistoryModel(String ticketNumber, String ticketDate) {
        this.ticketNumber = ticketNumber;
        this.ticketDate = ticketDate;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(String ticketDate) {
        this.ticketDate = ticketDate;
    }
}
