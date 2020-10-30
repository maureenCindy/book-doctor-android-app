package com.project.doctorinsta;

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

public class PatientDashboardActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPrefs sharedPrefs;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        actionBar = getSupportActionBar();
        sharedPrefs=SharedPrefs.getInstance(this);
        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_specialisations, R.id.nav_doctors, R.id.nav_profile, R.id.nav_bookings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_specialisations:
                    navController.navigate(R.id.nav_specialisations);
                    break;
                case R.id.nav_doctors:
                    navController.navigate(R.id.nav_doctors);
                    break;
                case R.id.nav_bookings:
                    navController.navigate(R.id.nav_bookings);
                    break;
                case R.id.nav_profile:
                    navController.navigate(R.id.nav_profile);
                    break;
                default:
                    navController.navigate(R.id.nav_specialisations);
                    break;
            }
            return false;
        });
        Intent intent = getIntent();
        String fragmentName = intent.getStringExtra("fragmentName");
        int specialisationIdNumber = intent.getIntExtra("specialisationIdNumber",1);
        sharedPrefs.setIntValue("specialisationId",specialisationIdNumber);
        if(fragmentName!=null){
            switch (fragmentName){
                case "Specialisation":
                    navController.navigate(R.id.nav_specialisations);
                    break;
                case "Profile":
                    navController.navigate(R.id.nav_profile);
                    break;
                case "Doctors":
                    navController.navigate(R.id.nav_doctors);
                    break;
                case "MyBookings":
                    navController.navigate(R.id.nav_bookings);
                    break;
                default:
                    navController.navigate(R.id.nav_specialisations);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}