package com.olympics.easypay.ui.registration;

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

import com.olympics.easypay.R;
import com.olympics.easypay.models.TokenModel;
import com.olympics.easypay.network.RetroHelper;
import com.olympics.easypay.ui.home.MainActivity;
import com.olympics.easypay.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.olympics.easypay.utils.Constants.BASE_URL;
import static com.olympics.easypay.utils.Constants.EMAIL;
import static com.olympics.easypay.utils.Constants.PASS;
import static com.olympics.easypay.utils.Constants.SHARED_PREFS;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
    EditText emailEdt, passEdt;
    TextView gotoSignUpBtn;
    Button confirmSignIn;
    CheckBox checkBox;
    Retrofit retrofit;
    RetroHelper helper;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initViews();
        initListeners();
        initPrefs();
        initRetrofit();
    }

    private void initPrefs() {
        sharedPreferences = getSharedPreferences(SHARED_PREFS, 0);
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        helper = retrofit.create(RetroHelper.class);
    }

    private void initListeners() {
        gotoSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSignUp();
            }
        });
        confirmSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEdt.getText().toString().trim();
                String pass = passEdt.getText().toString().trim();
                if (check(email, pass)) {
                    login(email, pass);
                }
            }
        });
    }

    private boolean check(String email, String pass) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "enter email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass.length() < 8) {
            Toast.makeText(getApplicationContext(), "enter pass", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
                                .putString(EMAIL, email)
                                .putString(PASS, pass)
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
                Toast.makeText(SignInActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoSignUp() {
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        finish();
    }

    private void gotoMain() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void initViews() {
        emailEdt = findViewById(R.id.email_signin);
        passEdt = findViewById(R.id.pass_signin);
        gotoSignUpBtn = findViewById(R.id.goto_signUp);
        confirmSignIn = findViewById(R.id.confirm_signIn);
        checkBox=findViewById(R.id.chk);
    }
}
