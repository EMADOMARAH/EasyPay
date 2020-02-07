package com.olympics.easypay.ui.wizard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.olympics.easypay.R;
import com.olympics.easypay.ui.registration.SignInActivity;
import com.olympics.easypay.utils.Constants;

public class WizardActivity extends AppCompatActivity implements WizardFragment.WizardListener {

    ViewPager2 viewPager;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        initShared();
        initViews();
    }

    private void initShared() {
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, 0);
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager_wizard);
        viewPager.setAdapter(new WizardAdapter(getSupportFragmentManager(), getLifecycle()));
        viewPager.setUserInputEnabled(false);
    }

    @Override
    public void onNext() {
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onFinish() {
        sharedPreferences
                .edit()
                .putBoolean(Constants.FIRST_TIME, false)
                .apply();
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        finish();
    }
}
