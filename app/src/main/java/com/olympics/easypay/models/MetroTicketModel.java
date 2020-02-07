package com.olympics.easypay.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MetroTicketModel implements Parcelable {
    @SerializedName("Ticket_number")
    private int ticketNumber;

    @SerializedName("start_Station")
    private String startStation;

    @SerializedName("End_Station")
    private String endStation;

    @SerializedName("ticket_date")
    private String ticketDate;

    @SerializedName("Metro_cost")
    private int metroCost;

    public MetroTicketModel(int ticketNumber, String startStation, String endStation, String ticketDate, int metroCost) {
        this.ticketNumber = ticketNumber;
        this.startStation = startStation;
        this.endStation = endStation;
        this.ticketDate = ticketDate;
        this.metroCost = metroCost;
    }

    public static final Creator<MetroTicketModel> CREATOR = new Creator<MetroTicketModel>() {
        @Override
        public MetroTicketModel createFromParcel(Parcel in) {
            return new MetroTicketModel(in);
        }

        @Override
        public MetroTicketModel[] newArray(int size) {
            return new MetroTicketModel[size];
        }
    };

    private MetroTicketModel(Parcel in) {
        ticketNumber = in.readInt();
        startStation = in.readString();
        endStation = in.readString();
        ticketDate = in.readString();
        metroCost = in.readInt();
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
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

    public int getMetroCost() {
        return metroCost;
    }

    public void setMetroCost(int metroCost) {
        this.metroCost = metroCost;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ticketNumber);
        dest.writeString(startStation);
        dest.writeString(endStation);
        dest.writeString(ticketDate);
        dest.writeInt(metroCost);
    }
}
