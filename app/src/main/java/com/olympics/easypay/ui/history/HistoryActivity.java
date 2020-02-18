package com.olympics.easypay.ui.history;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.olympics.easypay.R;
import com.olympics.easypay.models.ChargeHistoryModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.ui.home.MainActivity;
import com.olympics.easypay.ui.menu.about.AboutActivity;
import com.olympics.easypay.ui.menu.help.HelpActivity;
import com.olympics.easypay.ui.menu.settings.SettingsActivity;
import com.olympics.easypay.ui.registration.SignInActivity;
import com.olympics.easypay.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.olympics.easypay.utils.Constants.CARD;
import static com.olympics.easypay.utils.Constants.EMAIL;
import static com.olympics.easypay.utils.Constants.PASS;
import static com.olympics.easypay.utils.Constants.TOKEN;

public class HistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HistoryAdapter.ChargeHistoryListener {

    private static final String TAG = "MyTag";
    HistoryAdapter adapter;
    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.left_zero, R.anim.zero_right);
        finishAfterTransition();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initToolbar();
        initDrawer();
        initViews();
        initRetro();
    }

    private void initRetro() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, 0);
        int myId = sharedPreferences.getInt(Constants.TOKEN, 0);
        MyRetroFitHelper.getInstance().getPaymentHistory(myId).enqueue(new Callback<List<ChargeHistoryModel>>() {
            @Override
            public void onResponse(Call<List<ChargeHistoryModel>> call, Response<List<ChargeHistoryModel>> response) {
                if (response.isSuccessful()) {
                    List<ChargeHistoryModel> list = response.body();
                    if (list == null) {
                        showError();
                        return;
                    }
                    if (list.get(0) == null) {
                        showError();
                        return;
                    }
                    if (list.get(0).getChargeDate() == null) {
                        showError();
                        return;
                    }
                    if (list.get(0).getChargeDate().equals("null")) {
                        showError();
                        return;
                    }
                    if (list.get(0).getChargeDate().equals("NULL")) {
                        showError();
                        return;
                    }
                    if (list.isEmpty()) {
                        showError();
                        return;
                    }
                    adapter.setChargeHistoryModelList(response.body());
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<List<ChargeHistoryModel>> call, Throwable t) {
                Toast.makeText(HistoryActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailurePaymentHistory: " + t.toString());
            }
        });
    }

    private void showError() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ErrorHistoryFragment())
                .commit();
    }

    private void initViews() {
        adapter = new HistoryAdapter(new ArrayList<ChargeHistoryModel>(), this);

        recyclerView = findViewById(R.id.recycler_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
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
                overridePendingTransition(R.anim.right_zero, R.anim.zero_left);
                return true;
            case R.id.info:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                overridePendingTransition(R.anim.right_zero, R.anim.zero_left);
                return true;
            case R.id.help:
                startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                overridePendingTransition(R.anim.right_zero, R.anim.zero_left);
                return true;
            case R.id.logout:
                getSharedPreferences(Constants.SHARED_PREFS, 0)
                        .edit()
                        .remove(TOKEN)
                        .remove(EMAIL)
                        .remove(PASS)
                        .remove(CARD)
                        .apply();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.left_zero, R.anim.zero_right);
                finishAfterTransition();
                return true;
        }
        return false;
    }

    @Override
    public void onItemSelected(int position) {

    }
}
