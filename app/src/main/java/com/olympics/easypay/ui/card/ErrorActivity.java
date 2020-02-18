package com.olympics.easypay.ui.card;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.olympics.easypay.R;

public class ErrorActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        finishAfterTransition();
        overridePendingTransition(R.anim.left_zero, R.anim.zero_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
    }
}
