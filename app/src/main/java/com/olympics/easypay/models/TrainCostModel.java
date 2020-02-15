package com.olympics.easypay.models;

import com.google.gson.annotations.SerializedName;

public class TrainCostModel {
    @SerializedName("train_cost")
    private String trainCost;

    @SerializedName("balance")
    private String myBalance;

    public TrainCostModel(String trainCost, String myBalance) {
        this.trainCost = trainCost;
        this.myBalance = myBalance;
    }

    public String getTrainCost() {
        return trainCost;
    }

    public void setTrainCost(String trainCost) {
        this.trainCost = trainCost;
    }

    public String getMyBalance() {
        return myBalance;
    }

    public void setMyBalance(String myBalance) {
        this.myBalance = myBalance;
    }
}
