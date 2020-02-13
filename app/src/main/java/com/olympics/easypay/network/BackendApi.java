package com.olympics.easypay.network;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BackendApi {

    @FormUrlEncoded
    @POST("ephemeral_keys")
    Observable<ResponseBody> createEphemeralKey(
            @FieldMap Map<String, String> map
    );
}
