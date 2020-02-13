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
import com.olympics.easypay.models.EmailCheckModel;
import com.olympics.easypay.models.PhoneCheckModel;
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

import static com.olympics.easypay.utils.Constants.PASSWORD_PATTERN;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
    EditText nameEdt, emailEdt, passEdt, phoneEdt;
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
                String phone = phoneEdt.getText().toString().trim();
                String pass = passEdt.getText().toString().trim();
                if (check(name, email, phone, pass)) {
                    checkEmail(name, email, phone, pass);
                }
            }
        });
    }

    private boolean check(String name, String email, String phone, String pass) {
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!email.endsWith("@gmail.com") && !email.endsWith("@Gmail.com") && !email.endsWith("@outlook.com") && !email.endsWith("@Outlook.com")) {
            Toast.makeText(getApplicationContext(), "Email badly formatted", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Email badly formatted", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter your phone", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!phone.startsWith("01")) {
            Toast.makeText(getApplicationContext(), "Phone badly formatted", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.PHONE.matcher(phone).matches()) {
            Toast.makeText(getApplicationContext(), "Phone badly formatted", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass.length() < 8) {
            Toast.makeText(getApplicationContext(), "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!PASSWORD_PATTERN.matcher(pass).matches()) {
            Toast.makeText(getApplicationContext(), "Weak password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void checkEmail(final String name, final String email, final String phone, final String pass) {
        helper.checkEmailForSignUp(email).enqueue(new Callback<List<EmailCheckModel>>() {
            @Override
            public void onResponse(Call<List<EmailCheckModel>> call, Response<List<EmailCheckModel>> response) {
                if (response.isSuccessful()) {
                    String s = response.body().get(0).getEmail();
                    if (s.equals("Email Available")) {
                        checkPhone(name, email, phone, pass);
                    } else {
                        Toast.makeText(SignUpActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<EmailCheckModel>> call, Throwable t) {
                Log.d(TAG, "onFailureEmail: " + t.toString());
                Toast.makeText(SignUpActivity.this, "invalid email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPhone(final String name, final String email, final String phone, final String pass) {
        helper.checkPhone(phone).enqueue(new Callback<List<PhoneCheckModel>>() {
            @Override
            public void onResponse(Call<List<PhoneCheckModel>> call, Response<List<PhoneCheckModel>> response) {
                if (response.isSuccessful()) {
                    String s = response.body().get(0).getResult();
                    if (s.equals("possible signup")) {
                        signup(name, email, phone, pass);
                    } else {
                        Toast.makeText(SignUpActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PhoneCheckModel>> call, Throwable t) {
                Log.d(TAG, "onFailurePhone: " + t.toString());
                Toast.makeText(SignUpActivity.this, "invalid phone", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signup(String name, final String email, String phone, final String pass) {
        helper.signup(name, email, pass, phone).enqueue(new Callback<List<TokenModel>>() {
            @Override
            public void onResponse(Call<List<TokenModel>> call, Response<List<TokenModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body().get(0).getId().equals("Exist")) {
                        Toast.makeText(SignUpActivity.this, "user already exits", Toast.LENGTH_SHORT).show();
                    } else {
                        gotoSignIn();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TokenModel>> call, Throwable t) {
                Log.d(TAG, "onFailureSignUp: " + t.toString());
                Toast.makeText(SignUpActivity.this, "error sign up", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void login(final String email, final String pass) {
//        helper.login(email, pass).enqueue(new Callback<List<TokenModel>>() {
//            @Override
//            public void onResponse(Call<List<TokenModel>> call, Response<List<TokenModel>> response) {
//                if (response.isSuccessful()) {
//                    int token = Integer.parseInt(response.body().get(0).getId());
//                    if (checkBox.isChecked()) {
//                        sharedPreferences
//                                .edit()
//                                .putString(Constants.EMAIL, email)
//                                .putString(Constants.PASS, pass)
//                                .apply();
//                    } else {
//                        sharedPreferences
//                                .edit()
//                                .remove(EMAIL)
//                                .remove(PASS)
//                                .apply();
//                    }
//                    sharedPreferences
//                            .edit()
//                            .putInt(Constants.TOKEN, token)
//                            .apply();
//                    gotoMain();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<TokenModel>> call, Throwable t) {
//                Log.d(TAG, "onFailureLogin: " + t.toString());
//                Toast.makeText(SignUpActivity.this, "error login", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

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
        phoneEdt = findViewById(R.id.phone_signup);
        passEdt = findViewById(R.id.pass_signup);
        checkBox = findViewById(R.id.chk);
    }
}
