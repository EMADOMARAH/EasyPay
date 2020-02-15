package com.olympics.easypay.ui.registration;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.olympics.easypay.R;
import com.olympics.easypay.models.EmailCheckModel;
import com.olympics.easypay.models.PhoneCheckModel;
import com.olympics.easypay.models.TokenModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.utils.FieldValidator;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ConstantConditions")
public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
    TextInputLayout nameEdt, emailEdt, passEdt, phoneEdt;
    TextView gotoSignInBtn;
    Button confirmSignUp;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();
    }

    private boolean validateName() {
        return FieldValidator.checkName(nameEdt);
    }

    private boolean validateEmail() {
        return FieldValidator.checkEmail(emailEdt);
    }

    private boolean validatePhone() {
        return FieldValidator.checkPhone(phoneEdt);
    }

    private boolean validatePass() {
        return FieldValidator.checkPassword(passEdt);
    }

    private void checkEmail(final String name, final String email, final String phone, final String pass) {
        MyRetroFitHelper.getInstance().checkEmailForSignUp(email).enqueue(new Callback<List<EmailCheckModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<EmailCheckModel>> call, @NotNull Response<List<EmailCheckModel>> response) {
                if (response.isSuccessful()) {
                    String s = response.body().get(0).getEmail();
                    if (s.equals("Email Available")) {
                        checkPhone(name, email, phone, pass);
                        emailEdt.setError(null);
                    } else {
                        emailEdt.setError("Email already exists");
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<EmailCheckModel>> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailureEmail: " + t.toString());
                Toast.makeText(SignUpActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPhone(final String name, final String email, final String phone, final String pass) {
        MyRetroFitHelper.getInstance().checkPhone(phone).enqueue(new Callback<List<PhoneCheckModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<PhoneCheckModel>> call, @NotNull Response<List<PhoneCheckModel>> response) {
                if (response.isSuccessful()) {
                    String s = response.body().get(0).getResult();
                    if (s.equals("possible signup")) {
                        signup(name, email, phone, pass);
                        phoneEdt.setError(null);
                    } else {
                        phoneEdt.setError("Phone number used before");
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<PhoneCheckModel>> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailurePhone: " + t.toString());
                Toast.makeText(SignUpActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signup(String name, final String email, String phone, final String pass) {
        MyRetroFitHelper.getInstance().signup(name, email, pass, phone).enqueue(new Callback<List<TokenModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<TokenModel>> call, @NotNull Response<List<TokenModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body().get(0).getId().equals("Exist")) {
                        emailEdt.setError("Email already exists");
                    } else {
                        emailEdt.setError(null);
                        Toast.makeText(SignUpActivity.this, "Account created successfully!\n...please sign in", Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gotoSignIn();
                            }
                        }, 2000);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<TokenModel>> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailureSignUp: " + t.toString());
                Toast.makeText(SignUpActivity.this, "Server error", Toast.LENGTH_SHORT).show();
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
        overridePendingTransition(R.anim.left_zero, R.anim.zero_right);
        finishAfterTransition();
    }

//    private void gotoMain() {
//        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        finish();
//    }

    private void initViews() {
        gotoSignInBtn = findViewById(R.id.goto_signIn);
        confirmSignUp = findViewById(R.id.confirm_signUp);
        nameEdt = findViewById(R.id.name_signup);
        emailEdt = findViewById(R.id.email_signup);
        phoneEdt = findViewById(R.id.phone_signup);
        passEdt = findViewById(R.id.pass_signup);
        checkBox = findViewById(R.id.chk);

        gotoSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSignIn();
            }
        });
        confirmSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdt.getEditText().getText().toString().trim();
                String email = emailEdt.getEditText().getText().toString().trim();
                String phone = phoneEdt.getEditText().getText().toString().trim();
                String pass = passEdt.getEditText().getText().toString().trim();
                if (validateName() & validateEmail() & validatePhone() & validatePass()) {
                    checkEmail(name, email, phone, pass);
                }
            }
        });
    }
}
