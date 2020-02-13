package com.olympics.easypay.ui;

import android.app.Application;

import com.stripe.android.PaymentConfiguration;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(this, "pk_test_0c3kXTathaLjIJU7JF2ilzAk00NzKC4qLi");
    }
}
