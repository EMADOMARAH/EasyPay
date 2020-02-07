package com.example.easypay.ui.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.easypay.R;
import com.example.easypay.ui.qrcode.my.MyQrFragment;
import com.example.easypay.ui.qrcode.scan.ScanQrFragment;
import com.example.easypay.ui.registration.SignInActivity;
import com.example.easypay.ui.settings.SettingsActivity;
import com.example.easypay.utils.Constants;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class QrActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        initToolbar();
        initDrawer();
        initViews();
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
        viewPager = findViewById(R.id.viewPager_qr);
        tabLayout = findViewById(R.id.tabLayout_qr);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            Fragment[] fragments = new Fragment[]{
                    new ScanQrFragment(),
                    new MyQrFragment()
            };

            String[] strings = new String[]{
                    "Scan code", "My code"
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
                return true;
            case R.id.info:
                return true;
            case R.id.help:
                return true;
            case R.id.logout:
                getSharedPreferences(Constants.SHARED_PREFS, 0)
                        .edit()
                        .remove(Constants.TOKEN)
                        .remove(Constants.EMAIL)
                        .remove(Constants.PASS).apply();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                return true;
        }
        return false;
    }
}
