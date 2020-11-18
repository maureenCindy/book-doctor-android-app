package com.project.doctorinsta.doctor_ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.doctorinsta.R;
import com.project.doctorinsta.common_ui.ChangePasswordFragment;
import com.project.doctorinsta.utils.SharedPrefs;

public class DoctorDashboardActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPrefs sharedPrefs;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);
        actionBar = getSupportActionBar();
        sharedPrefs = SharedPrefs.getInstance(this);
        BottomNavigationView navigationView = findViewById(R.id.doc_nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_bookings, R.id.nav_schedules, R.id.nav_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_bookings:
                    navController.navigate(R.id.nav_bookings);
                    break;
                case R.id.nav_schedules:
                    navController.navigate(R.id.nav_schedules);
                    break;
                case R.id.nav_profile:
                    navController.navigate(R.id.nav_profile);
                    break;
                default:
                    navController.navigate(R.id.nav_bookings);
                    break;
            }
            return false;
        });

        Intent intent = getIntent();
        String fragmentName = intent.getStringExtra("fragmentName");
        if (fragmentName != null) {
            if (fragmentName.equalsIgnoreCase("ChangePassword")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        ChangePasswordFragment.newInstance()).commit();
                actionBar.setTitle("Change Password");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}