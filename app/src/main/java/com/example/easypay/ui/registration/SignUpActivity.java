package com.example.easypay.ui.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easypay.R;
import com.example.easypay.models.TokenModel;
import com.example.easypay.network.RetroHelper;
import com.example.easypay.ui.home.MainActivity;
import com.example.easypay.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
    EditText nameEdt, emailEdt, passEdt;
    TextView gotoSignInBtn;
    Button confirmSignUp;
    CheckBox checkBox;
    Retrofit retrofit;
    RetroHelper helper;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();
        initListeners();
        initPrefs();
        initRetrofit();
    }

    private void initPrefs() {
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, 0);
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        helper = retrofit.create(RetroHelper.class);
    }

    private void initListeners() {
        gotoSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSignIn();
            }
        });
        confirmSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdt.getText().toString().trim();
                String email = emailEdt.getText().toString().trim();
                String pass = passEdt.getText().toString().trim();
                if (check(name, email, pass)) {
                    signUp(name, email, pass);
                }
            }
        });
    }

    private boolean check(String name, String email, String pass) {
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "enter your email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass.length() < 8) {
            Toast.makeText(getApplicationContext(), "enter your password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void signUp(String name, final String email, final String pass) {
        helper.signup(name, email, pass).enqueue(new Callback<List<TokenModel>>() {
            @Override
            public void onResponse(Call<List<TokenModel>> call, Response<List<TokenModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body().get(0).getId().equals("Exist")) {
                        Toast.makeText(SignUpActivity.this, "user already exits", Toast.LENGTH_SHORT).show();
                    } else {
                        login(email, pass);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TokenModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(final String email, final String pass) {
        helper.login(email, pass).enqueue(new Callback<List<TokenModel>>() {
            @Override
            public void onResponse(Call<List<TokenModel>> call, Response<List<TokenModel>> response) {
                if (response.isSuccessful()) {
                    int token = Integer.parseInt(response.body().get(0).getId());
                    if (checkBox.isChecked()) {
                        sharedPreferences
                                .edit()
                                .putString(Constants.EMAIL, email)
                                .putString(Constants.PASS, pass)
                                .apply();
                    }
                    sharedPreferences
                            .edit()
                            .putInt(Constants.TOKEN, token)
                            .apply();
                    gotoMain();
                }
            }

            @Override
            public void onFailure(Call<List<TokenModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoSignIn() {
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        finish();
    }

    private void gotoMain() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void initViews() {
        gotoSignInBtn = findViewById(R.id.goto_signIn);
        confirmSignUp = findViewById(R.id.confirm_signUp);
        nameEdt = findViewById(R.id.name_signup);
        emailEdt = findViewById(R.id.email_signup);
        passEdt = findViewById(R.id.pass_signup);
        checkBox = findViewById(R.id.chk);
    }
}
