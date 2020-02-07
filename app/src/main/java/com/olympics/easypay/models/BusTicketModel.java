package com.olympics.easypay.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BusTicketModel implements Parcelable {
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

    public static final Creator<BusTicketModel> CREATOR = new Creator<BusTicketModel>() {
        @Override
        public BusTicketModel createFromParcel(Parcel in) {
            return new BusTicketModel(in);
        }

        @Override
        public BusTicketModel[] newArray(int size) {
            return new BusTicketModel[size];
        }
    };

    private BusTicketModel(Parcel in) {
        ticketNumber = in.readInt();
        lineNumber = in.readInt();
        ticketDate = in.readString();
        lineCost = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ticketNumber);
        dest.writeInt(lineNumber);
        dest.writeString(ticketDate);
        dest.writeInt(lineCost);
    }
}
