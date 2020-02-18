package com.olympics.easypay.ui.card;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.olympics.easypay.R;
import com.olympics.easypay.models.CardNumberModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.ui.menu.about.AboutActivity;
import com.olympics.easypay.ui.menu.help.HelpActivity;
import com.olympics.easypay.ui.menu.settings.SettingsActivity;
import com.olympics.easypay.ui.registration.SignInActivity;
import com.olympics.easypay.utils.Constants;

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

@SuppressWarnings({"NullableProblems", "ConstantConditions"})
public class CardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MyTag";
    Spinner spinner;
    EditText editText;
    TextView add, del;
    Button charge;
    float dp;
    int col;
    BigInteger selectedCardNumber;
    SharedPreferences sharedPreferences;
    List<BigInteger> cardNumbers;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    private int id;

    @Override
    public void onBackPressed() {
        finishAfterTransition();
        overridePendingTransition(R.anim.left_zero, R.anim.zero_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, 0);

        initToolbar();
        initDrawer();
        initViews();
        initData();
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

    private void initData() {
        id = getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        MyRetroFitHelper.getInstance()
                .getMyCardNumbers(id)
                .enqueue(new Callback<List<CardNumberModel>>() {
                    @Override
                    public void onResponse(Call<List<CardNumberModel>> call, Response<List<CardNumberModel>> response) {
                        if (response.isSuccessful()) {
                            initSpinner(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CardNumberModel>> call, Throwable t) {
                        initSpinner(new ArrayList<CardNumberModel>());
                        Log.d(TAG, "onFailureMyCards: " + t.toString());
                        Toast.makeText(CardActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initSpinner(List<CardNumberModel> cardNumberModelList) {
        cardNumbers = new ArrayList<>();
        final List<String> strings = new ArrayList<>();
        strings.add(getResources().getString(R.string.choose_card));
        for (CardNumberModel cardNumberModel : cardNumberModelList) {
            cardNumbers.add(cardNumberModel.getCardNumber());
            StringBuilder s = new StringBuilder(cardNumberModel.getCardNumber().toString());
            int t = 16 - s.length();
            for (int i = 0; i < t; i++) {
                s.insert(0, "0");
            }
            s.replace(0, 12, "XXXX XXXX XXXX ");
            strings.add(s.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, strings) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = new TextView(parent.getContext());
                textView.setText(strings.get(position));
                textView.setPadding((int) (16 * dp), (int) (16 * dp), (int) (16 * dp), (int) (16 * dp));
                if (position == 0) {
                    textView.setTextColor(col);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return textView;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = new TextView(parent.getContext());
                textView.setText(strings.get(position));
                textView.setPadding((int) (32 * dp), 0, (int) (32 * dp), 0);
                if (position == 0) {
                    textView.setTextColor(col);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return textView;
            }
        };
        spinner.setAdapter(adapter);
        if (sharedPreferences.contains(CARD)) {
            if (!sharedPreferences.getString(CARD, "").isEmpty()) {
                BigInteger bigInteger = BigInteger.valueOf(Long.valueOf(sharedPreferences.getString(CARD, "")));
                if (cardNumbers.contains(bigInteger)) {
                    spinner.setSelection(cardNumbers.indexOf(bigInteger) + 1);
                }
            }
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedCardNumber = cardNumbers.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initViews() {
        dp = getResources().getDisplayMetrics().density;
        col = getResources().getColor(R.color.greyish);

        charge = findViewById(R.id.charge);
        editText = findViewById(R.id.amount);
        spinner = findViewById(R.id.spinner);
        add = findViewById(R.id.add);
        del = findViewById(R.id.delete);

        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(CardActivity.this, "Enter amount to charge", Toast.LENGTH_SHORT).show();
                    return;
                }
                int am = Integer.parseInt(editText.getText().toString().trim());
                if (am % 10 != 0) {
                    Toast.makeText(CardActivity.this, "Charge with divisible by tens only", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedCardNumber == null) {
                    Toast.makeText(CardActivity.this, "Select a card", Toast.LENGTH_SHORT).show();
                    return;
                }
                chargeNow(selectedCardNumber, am);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCard();
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delCard();
            }
        });
//        conf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = spinner.getSelectedItemPosition();
//                if (position > 0) {
//                    sharedPreferences
//                            .edit()
//                            .putString(CARD, cardNumbers.get(position - 1).toString())
//                            .apply();
//                    onBackPressed();
//                }
//            }
//        });
    }

    private void chargeNow(BigInteger card, int amount) {
        MyRetroFitHelper.getInstance().charge(id, card, amount).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CardActivity.this, "Charged Successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailureCharge: " + t.toString());
                Toast.makeText(CardActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCard() {
        startActivity(new Intent(getApplicationContext(), CardAddActivity.class));
        overridePendingTransition(R.anim.right_zero, R.anim.zero_left);
    }

    private void delCard() {
        startActivity(new Intent(getApplicationContext(), CardDeleteActivity.class));
        overridePendingTransition(R.anim.right_zero, R.anim.zero_left);
    }

//    private void deleteCard(BigInteger cardNo) {
//        MyRetroFitHelper.getInstance().deleteCard(id, cardNo).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(CardActivity.this, "Card Deleted", Toast.LENGTH_SHORT).show();
//                    initData();
//                } else {
//                    Toast.makeText(CardActivity.this, "failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.d(TAG, "onFailureDeleteCard: " + t.toString());
//                Toast.makeText(CardActivity.this, "Server error", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

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
}
