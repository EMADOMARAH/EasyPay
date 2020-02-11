package com.olympics.easypay.ui.card;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.olympics.easypay.R;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.ui.registration.SignInActivity;
import com.olympics.easypay.ui.settings.SettingsActivity;
import com.olympics.easypay.utils.Constants;

import java.math.BigInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.olympics.easypay.utils.Constants.CARD;

public class CardAddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final int ANIM_DUR = 250;
    static final String TAG = "MyTag";
    final Handler handler = new Handler();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    TextView swipe;
    EditText cardTxt, mName, exDate, cvvTxt;
    Button confirm;
    FrameLayout front, back, cont;
    AnimatorSet rightBack, rightFront, leftBack, leftFront;
    boolean isFlipped = false;
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isFlipped) {
                front.setVisibility(View.VISIBLE);
                back.setVisibility(View.INVISIBLE);
                cont.setScaleX(1f);
                isFlipped = false;
            } else {
                front.setVisibility(View.INVISIBLE);
                back.setVisibility(View.VISIBLE);
                cont.setScaleX(-1f);
                isFlipped = true;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_add);

        initToolbar();
        initDrawer();
        initViews();
    }

    @SuppressLint("ClickableViewAccessibility")
    void initViews() {
        swipe = findViewById(R.id.swipeManually);
        mName = findViewById(R.id.holderName);
        exDate = findViewById(R.id.expiryDate);
        cardTxt = findViewById(R.id.cvvTxt);
        confirm = findViewById(R.id.payNow);
        cvvTxt = findViewById(R.id.cvv2);

        cont = findViewById(R.id.container_cards);
        front = findViewById(R.id.front);
        back = findViewById(R.id.back);

        cont.setCameraDistance(8000 * getResources().getDisplayMetrics().density);

        rightBack = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.rotate_right_back);
        rightFront = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.rotate_right_front);
        leftBack = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.rotate_left_back);
        leftFront = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.rotate_left_front);

        swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });
        cardTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cardTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (' ' == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    char c = s.charAt(s.length() - 1);
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(' ')).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(' '));
                    }
                }
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check())
                    return;
                BigInteger card = BigInteger.valueOf(Long.parseLong(cardTxt.getText().toString().replace(" ", "").trim()));
                int cvv = Integer.valueOf(cvvTxt.getText().toString().trim());
                String holderName = mName.getText().toString().trim();
                int m = Integer.valueOf(exDate.getText().toString().trim().split("/")[0]);
                int y = Integer.valueOf(exDate.getText().toString().trim().split("/")[1]);
                setCredits(card, cvv, holderName, m, y);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    void flipCard() {
        if (isFlipped) {
            cont.animate().rotationY(0).setDuration(ANIM_DUR).start();
        } else {
            cont.animate().rotationY(180).setDuration(ANIM_DUR).start();
        }
        handler.postDelayed(runnable, ANIM_DUR / 2);
    }

    boolean check() {
        if (cardTxt.getText().toString().length() != 19)
            return false;
        if (cvvTxt.getText().toString().isEmpty())
            return false;
        if (mName.getText().toString().isEmpty())
            return false;
        int m = Integer.valueOf(exDate.getText().toString().trim().split("/")[0]);
        int y = Integer.valueOf(exDate.getText().toString().trim().split("/")[1]);
        if (m <= 0 || m > 12)
            return false;
        if (y <= 2019)
            return false;
        //noinspection RedundantIfStatement
        if (exDate.getText().toString().isEmpty())
            return false;
        return true;
    }

    void setCredits(final BigInteger card, int cvv, String holderName, int m, int y) {
        final SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, 0);
        int id = sharedPreferences.getInt(Constants.TOKEN, 0);
        MyRetroFitHelper.getInstance().addCredit(id, card, cvv, m, y, holderName).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(CardAddActivity.this, "Success", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    sharedPreferences
                            .edit()
                            .putString(CARD, card.toString())
                            .apply();
                    onBackPressed();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(CardAddActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
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
                        .clear()
                        .apply();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), CardActivity.class));
        finish();
    }
}
