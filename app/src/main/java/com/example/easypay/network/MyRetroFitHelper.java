package com.example.easypay.network;

import com.example.easypay.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetroFitHelper {
    private static Retrofit retroFit;
    private static RetroHelper helper;

    public static RetroHelper getInstance() {
        if (retroFit == null) {
            retroFit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        if (helper == null) {
            helper = retroFit.create(RetroHelper.class);
        }
        return helper;
    }
}
