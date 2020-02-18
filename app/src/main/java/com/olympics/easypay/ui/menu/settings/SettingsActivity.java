package com.olympics.easypay.ui.menu.settings;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.olympics.easypay.R;
import com.olympics.easypay.models.GetSettingModel;
import com.olympics.easypay.models.PasswordCheckModel;
import com.olympics.easypay.models.SetSettingModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.network.RetroHelper;
import com.olympics.easypay.ui.menu.about.AboutActivity;
import com.olympics.easypay.ui.menu.help.HelpActivity;
import com.olympics.easypay.ui.registration.SignInActivity;
import com.olympics.easypay.utils.FieldValidator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.olympics.easypay.utils.Constants.CARD;
import static com.olympics.easypay.utils.Constants.EMAIL;
import static com.olympics.easypay.utils.Constants.PASS;
import static com.olympics.easypay.utils.Constants.SHARED_PREFS;
import static com.olympics.easypay.utils.Constants.TOKEN;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MyTag";
    TextInputLayout nameEdt, emailEdt, passEdt, phoneEdt;
    Button editBtn;
    RetroHelper helper;
    SharedPreferences sharedPreferences;
    int myId;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    @Override
    public void onBackPressed() {
        finishAfterTransition();
        overridePendingTransition(R.anim.left_zero, R.anim.zero_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initToolbar();
        initDrawer();
        initViews();
        initSharedPrefs();
        initRetroFit();
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

    private void initViews() {
        editBtn = findViewById(R.id.editButton);
        nameEdt = findViewById(R.id.name_setting);
        emailEdt = findViewById(R.id.email_setting);
        passEdt = findViewById(R.id.pass_setting);
        phoneEdt = findViewById(R.id.phone_setting);

        emailEdt.setEnabled(false);
        emailEdt.setClickable(false);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdt.getEditText().getText().toString().trim();
                String email = emailEdt.getEditText().getText().toString().trim();
                String pass = passEdt.getEditText().getText().toString().trim();
                String phone = phoneEdt.getEditText().getText().toString().trim();
                if (validateName() & validateEmail() & validatePhone() & validatePass()) {
                    setSettings(name, email, phone, pass);
                }
            }
        });
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

    private void setSettings(final String name, final String email, final String phone, final String pass) {
        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm);
        dialog.findViewById(R.id.conf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ((EditText) dialog.findViewById(R.id.pass)).getText().toString().trim();
                if (s.isEmpty()) {
                    Toast.makeText(SettingsActivity.this, "Enter old password", Toast.LENGTH_SHORT).show();
                    return;
                }
                helper.checkPassword(email, s).enqueue(new Callback<List<PasswordCheckModel>>() {
                    @Override
                    public void onResponse(Call<List<PasswordCheckModel>> call, Response<List<PasswordCheckModel>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().get(0).getPassword().equals("password successful")) {
                                helper.setSetting(myId, pass, name, phone).enqueue(new Callback<List<SetSettingModel>>() {
                                    @Override
                                    public void onResponse(Call<List<SetSettingModel>> call, Response<List<SetSettingModel>> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(SettingsActivity.this, response.body().get(0).getUpdate(), Toast.LENGTH_SHORT).show();
                                            getSharedPreferences(SHARED_PREFS, 0)
                                                    .edit()
                                                    .remove(TOKEN)
                                                    .remove(EMAIL)
                                                    .remove(PASS)
                                                    .remove(CARD)
                                                    .apply();
                                            startActivity(new Intent(getApplicationContext(), SignInActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                            onBackPressed();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<SetSettingModel>> call, Throwable t) {
                                        Toast.makeText(SettingsActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onFailureSetSetting: " + t.toString());
                                    }
                                });
                            } else {
                                Toast.makeText(SettingsActivity.this, response.body().get(0).getPassword(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PasswordCheckModel>> call, Throwable t) {
                        Log.d(TAG, "onFailureCheckPass: " + t.toString());
                        Toast.makeText(SettingsActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();
    }

    private void getSettings() {
        helper.getSetting(myId).enqueue(new Callback<List<GetSettingModel>>() {
            @Override
            public void onResponse(Call<List<GetSettingModel>> call, Response<List<GetSettingModel>> response) {
                if (response.isSuccessful()) {
                    String name = response.body().get(0).getName();
                    String email = response.body().get(0).getEmail();
                    String pass = response.body().get(0).getPassword();
                    String phone = response.body().get(0).getPhoneNumber();

                    nameEdt.getEditText().setText(name);
                    emailEdt.getEditText().setText(email);
                    passEdt.getEditText().setText(pass);
                    phoneEdt.getEditText().setText(phone);
                }
            }

            @Override
            public void onFailure(Call<List<GetSettingModel>> call, Throwable t) {
                Log.d(TAG, "onFailureGetSetting: " + t.toString());
                Toast.makeText(SettingsActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    private void initSharedPrefs() {
        sharedPreferences = getSharedPreferences(SHARED_PREFS, 0);
        myId = sharedPreferences.getInt(TOKEN, 0);
    }

    private void initRetroFit() {
        helper = MyRetroFitHelper.getInstance();
        getSettings();

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
                getSharedPreferences(SHARED_PREFS, 0)
                        .edit()
                        .remove(TOKEN)
                        .remove(EMAIL)
                        .remove(PASS)
                        .remove(CARD)
                        .apply();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                onBackPressed();
                return true;
        }
        return false;
    }
}
