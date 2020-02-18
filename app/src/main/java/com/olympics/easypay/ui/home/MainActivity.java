package com.olympics.easypay.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.olympics.easypay.R;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.ui.home.payment.PaymentFragment;
import com.olympics.easypay.ui.home.wallet.WalletFragment;
import com.olympics.easypay.ui.menu.about.AboutActivity;
import com.olympics.easypay.ui.menu.help.HelpActivity;
import com.olympics.easypay.ui.menu.settings.SettingsActivity;
import com.olympics.easypay.ui.registration.SignInActivity;
import com.olympics.easypay.utils.Constants;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.olympics.easypay.utils.Constants.CARD;
import static com.olympics.easypay.utils.Constants.EMAIL;
import static com.olympics.easypay.utils.Constants.PASS;
import static com.olympics.easypay.utils.Constants.TOKEN;

@SuppressWarnings({"ConstantConditions", "deprecation"})
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MyTag";
    ViewPager viewPager;
    TabLayout tabLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Exit")
                .setMessage("Do you want to exit EasyPay?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRetro();
        initToolbar();
        initDrawer();
        initViews();
    }

    private void initRetro() {
        MyRetroFitHelper.getInstance().show(getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {

            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Toast.makeText(MainActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailureShow: " + t.toString());
            }
        });
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

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager_main);
        tabLayout = findViewById(R.id.tabLayout_main);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            private Fragment[] fragments = new Fragment[]{
                    new PaymentFragment(),
                    new WalletFragment()
            };

            private String[] strings = new String[]{
                    "Payment", "Wallet"
            };

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return strings[position];
            }
        });
        tabLayout.setupWithViewPager(viewPager);
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
