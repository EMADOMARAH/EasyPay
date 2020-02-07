package com.example.easypay.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.easypay.R;
import com.example.easypay.models.GetSettingModel;
import com.example.easypay.models.SetSettingModel;
import com.example.easypay.network.MyRetroFitHelper;
import com.example.easypay.network.RetroHelper;
import com.example.easypay.ui.registration.SignInActivity;
import com.example.easypay.utils.Constants;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MyTag";
    EditText nameEdt, emailEdt, passEdt;
    Button editBtn;
    RetroHelper helper;
    SharedPreferences sharedPreferences;
    int myId;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initToolbar();
        initDrawer();
        initViews();
        initSharedPrefs();
        initRetroFit();
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

    private void initViews() {
        editBtn = findViewById(R.id.editButton);
        nameEdt = findViewById(R.id.name_setting);
        emailEdt = findViewById(R.id.email_setting);
        passEdt = findViewById(R.id.pass_setting);

        emailEdt.setEnabled(false);
        emailEdt.setClickable(false);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdt.getText().toString().trim();
                String email = emailEdt.getText().toString().trim();
                String pass = passEdt.getText().toString().trim();
                if (check(name, email, pass)) {
                    setSettings(name, email, pass);
                }
            }
        });
    }

    private boolean check(String name, String email, String pass) {
        if (name.isEmpty()) {
            Toast.makeText(this, "enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "enter your email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass.length() < 8) {
            Toast.makeText(this, "enter your password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setSettings(String name, String email, String pass) {
        helper.setSetting(myId, pass, name).enqueue(new Callback<List<SetSettingModel>>() {
            @Override
            public void onResponse(Call<List<SetSettingModel>> call, Response<List<SetSettingModel>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SettingsActivity.this, response.body().get(0).getUpdate(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<SetSettingModel>> call, Throwable t) {
                Toast.makeText(SettingsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void getSettings() {
        helper.getSetting(myId).enqueue(new Callback<List<GetSettingModel>>() {
            @Override
            public void onResponse(Call<List<GetSettingModel>> call, Response<List<GetSettingModel>> response) {
                if (response.isSuccessful()) {
                    String name = response.body().get(0).getName();
                    String email = response.body().get(0).getEmail();
                    String pass = response.body().get(0).getPassword();

                    nameEdt.setText(name);
                    emailEdt.setText(email);
                    passEdt.setText(pass);
                }
            }

            @Override
            public void onFailure(Call<List<GetSettingModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(SettingsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSharedPrefs() {
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, 0);
        myId = sharedPreferences.getInt(Constants.TOKEN, 0);
    }

    private void initRetroFit() {
        helper = MyRetroFitHelper.getInstance();
        getSettings();

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
                        .remove(Constants.PASS).apply();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                return true;
        }
        return false;
    }
}
