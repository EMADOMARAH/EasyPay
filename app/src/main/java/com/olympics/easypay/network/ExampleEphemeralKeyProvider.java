package com.olympics.easypay.network;

import androidx.annotation.NonNull;
import androidx.annotation.Size;

import com.stripe.android.EphemeralKeyProvider;
import com.stripe.android.EphemeralKeyUpdateListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExampleEphemeralKeyProvider implements EphemeralKeyProvider {
    private final BackendApi mBackendApi = new Retrofit
            .Builder()
            .baseUrl("https://api.stripe.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(BackendApi.class);
    private final CompositeDisposable mCompositeDisposable =
            new CompositeDisposable();

    @Override
    public void createEphemeralKey(@NonNull @Size(min = 4) String apiVersion, @NonNull final EphemeralKeyUpdateListener keyUpdateListener) {

        final Map<String, String> apiParamMap = new HashMap<>();
        apiParamMap.put("api_version", apiVersion);

        // Using RxJava2 for handling asynchronous responses
        mCompositeDisposable.add(mBackendApi.createEphemeralKey(apiParamMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<ResponseBody>() {
                            @Override
                            public void accept(ResponseBody response) {
                                try {
                                    final String rawKey = response.string();
                                    keyUpdateListener.onKeyUpdate(rawKey);
                                } catch (IOException ignored) {
                                }
                            }
                        }));
    }
}
