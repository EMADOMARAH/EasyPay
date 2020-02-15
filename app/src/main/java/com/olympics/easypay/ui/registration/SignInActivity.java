package com.olympics.easypay.ui.registration;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.olympics.easypay.R;
import com.olympics.easypay.models.EmailCheckModel;
import com.olympics.easypay.models.PasswordCheckModel;
import com.olympics.easypay.models.RetrievePasswordModel;
import com.olympics.easypay.models.TokenModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.network.RetroHelper;
import com.olympics.easypay.ui.home.MainActivity;
import com.olympics.easypay.utils.FieldValidator;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.olympics.easypay.utils.Constants.BASE_URL;
import static com.olympics.easypay.utils.Constants.EMAIL;
import static com.olympics.easypay.utils.Constants.PASS;
import static com.olympics.easypay.utils.Constants.SHARED_PREFS;
import static com.olympics.easypay.utils.Constants.TOKEN;

@SuppressWarnings("ConstantConditions")
public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";

    TextInputLayout emailEdt, passEdt;
    TextView gotoSignUpBtn, forgetBtn;
    Button confirmSignIn;
    CheckBox checkBox;
    Retrofit retrofit;
    RetroHelper helper;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    boolean isPendingFinish = false;

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
                String email = emailEdt.getEditText().getText().toString().trim();
                String pass = passEdt.getEditText().getText().toString().trim();
                if (validateEmail() & validatePass()) {
                    checkEmail(email, pass);
                }
            }
        });
        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(SignInActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_email_phone);
                dialog.findViewById(R.id.conf).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = ((EditText) dialog.findViewById(R.id.email)).getText().toString().trim();
                        String phone = ((EditText) dialog.findViewById(R.id.phone)).getText().toString().trim();
                        if (email.isEmpty() || phone.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || !Patterns.PHONE.matcher(phone).matches()) {
                            return;
                        }
                        retrieveToken(email, phone);
                    }
                });
                dialog.show();
            }
        });
    }

    private void retrieveToken(String email, String phone) {
        MyRetroFitHelper.getInstance().retrievePassword(email, phone).enqueue(new Callback<List<RetrievePasswordModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<RetrievePasswordModel>> call, @NotNull Response<List<RetrievePasswordModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body().get(0).getId() == null || response.body().get(0).getId().equals("NULL")) {
                        Toast.makeText(SignInActivity.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final int id = Integer.parseInt(response.body().get(0).getId());
                    final Dialog dialog = new Dialog(SignInActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_new_pass);
                    dialog.findViewById(R.id.conf).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextInputLayout txtLayout = dialog.findViewById(R.id.pass);
                            TextInputLayout txtLayout2 = dialog.findViewById(R.id.repass);
                            String pass = txtLayout.getEditText().getText().toString().trim();
                            String repass = txtLayout2.getEditText().getText().toString().trim();
                            if (FieldValidator.checkPassword(txtLayout) & FieldValidator.checkRePassword(txtLayout2, txtLayout)) {
                                loginWithNewPass(id, pass, repass);
                            }
                        }
                    });
                    dialog.show();

                }
            }

            @Override
            public void onFailure(@NotNull Call<List<RetrievePasswordModel>> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailureRetrieveToken: " + t.toString());
                Toast.makeText(SignInActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginWithNewPass(final int id, String pass, String repass) {
        MyRetroFitHelper.getInstance().changePassword(id, pass, repass).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(SignInActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                        sharedPreferences.edit().putInt(TOKEN, id).apply();
                        gotoMain();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailureChangePassword: " + t.toString());
                Toast.makeText(SignInActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateEmail() {
        return FieldValidator.checkEmail(emailEdt);
    }

    private boolean validatePass() {
        return FieldValidator.checkPassword(passEdt);
    }

    private void checkEmail(final String email, final String pass) {
        helper.checkEmailForLogin(email).enqueue(new Callback<List<EmailCheckModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<EmailCheckModel>> call, @NotNull Response<List<EmailCheckModel>> response) {
                if (response.isSuccessful()) {
                    String s = response.body().get(0).getEmail();
                    if (s.equals("Email Exist")) {
                        checkPass(email, pass);
                        emailEdt.setError(null);
                    } else {
                        emailEdt.setError("Email doesn't exist");
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<EmailCheckModel>> call, @NotNull Throwable t) {
                Toast.makeText(SignInActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailureEmail: " + t.toString());
            }
        });
    }

    private void checkPass(final String email, final String pass) {
        helper.checkPassword(email, pass).enqueue(new Callback<List<PasswordCheckModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<PasswordCheckModel>> call, @NotNull Response<List<PasswordCheckModel>> response) {
                if (response.isSuccessful()) {
                    String s1 = response.body().get(0).getPassword();
                    if (s1.equals("password successful")) {
                        login(email, pass);
                        passEdt.setError(null);
                    } else {
                        passEdt.setError("Wrong password");
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<PasswordCheckModel>> call, @NotNull Throwable t) {
                Toast.makeText(SignInActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailurePass: " + t.toString());
            }
        });
    }

    private void login(final String email, final String pass) {
        helper.login(email, pass).enqueue(new Callback<List<TokenModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<TokenModel>> call, @NotNull Response<List<TokenModel>> response) {
                if (response.isSuccessful()) {
                    int token = Integer.parseInt(response.body().get(0).getId());
                    if (checkBox.isChecked()) {
                        sharedPreferences
                                .edit()
                                .putString(EMAIL, email)
                                .putString(PASS, pass)
                                .apply();
                    } else {
                        sharedPreferences
                                .edit()
                                .remove(EMAIL)
                                .remove(PASS)
                                .apply();
                    }
                    Log.d(TAG, "onResponse: " + token);
                    sharedPreferences
                            .edit()
                            .putInt(TOKEN, token)
                            .apply();
                    gotoMain();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<TokenModel>> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailureLogin: " + t.toString());
                Toast.makeText(SignInActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoSignUp() {
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        overridePendingTransition(R.anim.right_zero, R.anim.zero_left);
        finishAfterTransition();
    }

    private void gotoMain() {
        isPendingFinish = true;
        startActivity(
                new Intent(getApplicationContext(), MainActivity.class),
                ActivityOptions.makeSceneTransitionAnimation(
                        SignInActivity.this, imageView, "lgo").toBundle()
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isPendingFinish) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        imageView.setTransitionName(null);
        finish();
    }

    private void initViews() {
        imageView = findViewById(R.id.imageView);
        forgetBtn = findViewById(R.id.frgt);
        emailEdt = findViewById(R.id.email_signin);
        passEdt = findViewById(R.id.pass_signin);
        gotoSignUpBtn = findViewById(R.id.goto_signUp);
        confirmSignIn = findViewById(R.id.confirm_signIn);
        checkBox = findViewById(R.id.chk);
    }
}
