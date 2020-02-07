package com.olympics.easypay.ui.splash;

import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.olympics.easypay.R;
import com.olympics.easypay.models.TokenModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.network.RetroHelper;
import com.olympics.easypay.ui.home.MainActivity;
import com.olympics.easypay.ui.registration.SignInActivity;
import com.olympics.easypay.ui.wizard.WizardActivity;
import com.olympics.easypay.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.olympics.easypay.utils.Constants.EMAIL;
import static com.olympics.easypay.utils.Constants.FIRST_TIME;
import static com.olympics.easypay.utils.Constants.PASS;
import static com.olympics.easypay.utils.Constants.SHARED_PREFS;
import static com.olympics.easypay.utils.Constants.TOKEN;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
    RetroHelper helper;
    SharedPreferences sharedPreferences;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            checkForAuth();
        }
    };

    private void checkForAuth() {
        if (sharedPreferences.contains(TOKEN)) {
            if (sharedPreferences.getInt(TOKEN, 0) > 0) {
                gotoMain();
                return;
            }
        }
        if (!sharedPreferences.contains(EMAIL) || !sharedPreferences.contains(PASS)) {
            gotoAuth();
            return;
        }
        if (sharedPreferences.getString(EMAIL, "").isEmpty() || sharedPreferences.getString(PASS, "").isEmpty()) {
            gotoAuth();
            return;
        }
        login(sharedPreferences.getString(EMAIL, ""), sharedPreferences.getString(PASS, ""));
    }

    private void login(String email, String pass) {
        helper.login(email, pass).enqueue(new Callback<List<TokenModel>>() {
            @Override
            public void onResponse(Call<List<TokenModel>> call, Response<List<TokenModel>> response) {
                if (response.isSuccessful()) {
                    int token = Integer.parseInt(response.body().get(0).getId());
                    sharedPreferences
                            .edit()
                            .putInt(Constants.TOKEN, token)
                            .apply();
                    gotoMain();
                }
            }

            @Override
            public void onFailure(Call<List<TokenModel>> call, Throwable t) {
                Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void gotoMain() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void gotoAuth() {
        if (checkForFirstTime()) {
            startActivity(new Intent(getApplicationContext(), WizardActivity.class));
            finish();
        } else {
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        }
    }

    private boolean checkForFirstTime() {
        if (sharedPreferences.contains(FIRST_TIME)) {
            return sharedPreferences.getBoolean(FIRST_TIME, true);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initPrefs();
        initRetro();
        animateProgress();
        startCounter();
    }

    private void initPrefs() {
        sharedPreferences = getSharedPreferences(SHARED_PREFS, 0);
    }

    private void initRetro() {
        helper = MyRetroFitHelper.getInstance();
    }

    private void startCounter() {
        new Handler().postDelayed(runnable, 3000);
    }

    private void animateProgress() {
        ImageView imageView = findViewById(R.id.progress_splash);

        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0, 360);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(4000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setEvaluator(new FloatEvaluator());
        animator.start();

        //TODO remove Hack
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                gotoMain();
                sharedPreferences.edit().putInt(TOKEN, 102).apply();
                return true;
            }
        });
    }
}
