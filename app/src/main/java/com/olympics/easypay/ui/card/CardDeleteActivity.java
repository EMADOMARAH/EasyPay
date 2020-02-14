package com.olympics.easypay.ui.card;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.olympics.easypay.R;
import com.olympics.easypay.models.CardNumberModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.network.RetroHelper;
import com.olympics.easypay.ui.registration.SignInActivity;
import com.olympics.easypay.ui.settings.SettingsActivity;
import com.olympics.easypay.utils.Constants;
import com.olympics.easypay.utils.Spacify;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.olympics.easypay.utils.Constants.CARD;
import static com.olympics.easypay.utils.Constants.EMAIL;
import static com.olympics.easypay.utils.Constants.PASS;
import static com.olympics.easypay.utils.Constants.TOKEN;

public class CardDeleteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int ANIM_DUR = 250;
    private static final String TAG = "MyTag";
    Spinner spinner;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    TextView cardNo;
    Button button;
    CardView cardView;
    float dp;
    RetroHelper helper;
    List<CardNumberModel> cardNumberModelList;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_delete);

        initToolbar();
        initDrawer();
        initViews();
        initData();
    }

    void initToolbar() {
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

    void initDrawer() {
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
                        .remove(TOKEN)
                        .remove(EMAIL)
                        .remove(PASS)
                        .remove(CARD)
                        .apply();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                return true;
        }
        return false;
    }

    private void initData() {
        helper = MyRetroFitHelper.getInstance();

        int id = getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        helper.getMyCardNumbers(id).enqueue(new Callback<List<CardNumberModel>>() {
            @Override
            public void onResponse(Call<List<CardNumberModel>> call, Response<List<CardNumberModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isEmpty()) {
                        Toast.makeText(CardDeleteActivity.this, "You don't have any cards", Toast.LENGTH_SHORT).show();
                    } else {
                        cardNumberModelList = response.body();
                        initCards(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CardNumberModel>> call, Throwable t) {
                Log.d(TAG, "onFailureCardNumbers: " + t.toString());
                Toast.makeText(CardDeleteActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initCards(List<CardNumberModel> cardNumberModelList) {
        final List<String> strings = new ArrayList<>();
        strings.add(getResources().getString(R.string.choose_card));
        for (CardNumberModel cardNumberModel : cardNumberModelList) {
            StringBuilder s = new StringBuilder(cardNumberModel.getCardNumber().toString());
            int t = 16 - s.length();
            for (int i = 0; i < t; i++) {
                s.insert(0, "0");
            }
            s.replace(0, 12, "XXXX XXXX XXXX ");
            strings.add(s.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = new TextView(parent.getContext());
                textView.setText(strings.get(position));
                textView.setPadding((int) (32 * dp), 0, (int) (32 * dp), 0);
                if (position == 0) {
                    textView.setTextColor(getResources().getColor(R.color.greyish));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                return textView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = new TextView(parent.getContext());
                textView.setText(strings.get(position));
                textView.setPadding((int) (8 * dp), (int) (8 * dp), (int) (8 * dp), (int) (8 * dp));
                if (position == 0) {
                    textView.setTextColor(getResources().getColor(R.color.greyish));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                return textView;
            }
        };
        spinner.setAdapter(adapter);
        initSpinner();
    }

    private void initSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItemPosition() != 0) {
                    if (cardNumberModelList != null) {
                        CardNumberModel cardNumberModel = cardNumberModelList.get(position - 1);
                        flip(cardNumberModel);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void flip(final CardNumberModel cardNumberModel) {
        cardView.animate().rotationY(180).setDuration(ANIM_DUR).start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cardNo.setText(Spacify.take(cardNumberModel.getCardNumber().toString()));
                cardView.setScaleX(-1f);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cardView.setScaleX(1f);
                        cardView.setRotationY(0);
                    }
                }, ANIM_DUR / 2);
            }
        }, ANIM_DUR / 2);
    }

    private void initViews() {
        dp = getResources().getDisplayMetrics().density;

        cardView = findViewById(R.id.cont);
        spinner = findViewById(R.id.spinner);
        button = findViewById(R.id.delete);
        cardNo = findViewById(R.id.cvvTxt);

        cardView.setCameraDistance(8000 * dp);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItemPosition() != 0) {
                    if (cardNumberModelList != null) {
                        deleteCard(cardNumberModelList.get(spinner.getSelectedItemPosition() - 1).getCardNumber());
                    }
                }
            }
        });
    }

    private void deleteCard(BigInteger cardNo) {
        int id = getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        helper.deleteCard(id, cardNo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CardDeleteActivity.this, "Card Deleted", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(CardDeleteActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(CardDeleteActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), CardActivity.class));
        finish();
    }
}
