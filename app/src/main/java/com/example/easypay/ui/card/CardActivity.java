package com.example.easypay.ui.card;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.easypay.R;
import com.example.easypay.network.MyRetroFitHelper;
import com.example.easypay.ui.registration.SignInActivity;
import com.example.easypay.ui.settings.SettingsActivity;
import com.example.easypay.utils.Constants;
import com.example.easypay.utils.Spacify;
import com.google.android.material.navigation.NavigationView;

import java.math.BigInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MyTag";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    EditText cardNo, cvvEdt, name, mon, yr, amount;
    TextView cvvTxt, mName, exDate;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        initToolbar();
        initDrawer();
        initViews();
    }

    private void initViews() {
        cardNo = findViewById(R.id.cardNo);
        cvvEdt = findViewById(R.id.cvvEdt);
        mName = findViewById(R.id.holderName);
        exDate = findViewById(R.id.expiryDate);
        mon = findViewById(R.id.monEdt);
        yr = findViewById(R.id.yrEdt);
        name = findViewById(R.id.nameEdt);
        amount = findViewById(R.id.amountEdt);
        cvvTxt = findViewById(R.id.cvvTxt);
        confirm = findViewById(R.id.payNow);

        cardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cvvTxt.setText("Card Number: " + Spacify.take(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mName.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ys = yr.getText().toString().trim();
                int y = 0;
                if (!ys.isEmpty()) {
                    y = Integer.valueOf(ys);
                }
                if (y > 2019 && y <= 2050) {
                    exDate.setText(s + "/" + ys);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        yr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ms = mon.getText().toString().trim();
                int m = 0;
                if (!ms.isEmpty()) {
                    m = Integer.valueOf(ms);
                }
                if (m >= 1 && m <= 12) {
                    exDate.setText(ms + "/" + s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigInteger card = BigInteger.valueOf(Long.parseLong(cardNo.getText().toString().trim()));
                int cvv = Integer.valueOf(cvvEdt.getText().toString().trim());
                String holderName = mName.getText().toString().trim();
                int m = Integer.valueOf(exDate.getText().toString().trim().split("/")[0]);
                int y = Integer.valueOf(exDate.getText().toString().trim().split("/")[1]);
                int am = Integer.valueOf(amount.getText().toString().trim());
                setCredits(card, cvv, holderName, m, y, am);
            }
        });
    }

    private void setCredits(BigInteger card, int cvv, String holderName, int m, int y, int am) {
        MyRetroFitHelper.getInstance().setCredits(
                getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0),
                card, cvv, m, y, holderName, am, "VISA"
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(CardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.goBack) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDrawer() {
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.side_nav);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);

        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.getHeaderView(0).findViewById(R.id.closeDrawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            case R.id.info:
                return true;
            case R.id.help:
                return true;
            case R.id.logout:
                getSharedPreferences(Constants.SHARED_PREFS, 0)
                        .edit()
                        .remove(Constants.TOKEN)
                        .remove(Constants.EMAIL)
                        .remove(Constants.PASS)
                        .apply();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
                return true;
        }
        return false;
    }
}
