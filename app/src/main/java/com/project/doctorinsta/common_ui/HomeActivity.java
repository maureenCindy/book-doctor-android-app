package com.project.doctorinsta.common_ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.project.doctorinsta.R;
import com.project.doctorinsta.doctor_ui.auth.DoctorLoginActivity;
import com.project.doctorinsta.doctor_ui.dashboard.DoctorDashboardActivity;
import com.project.doctorinsta.patient_ui.auth.LoginActivity;
import com.project.doctorinsta.patient_ui.dashboard.PatientDashboardActivity;
import com.project.doctorinsta.utils.SharedPrefs;

public class HomeActivity extends AppCompatActivity {

    private SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPrefs = SharedPrefs.getInstance(HomeActivity.this);
        findViewById(R.id.btnDoctor).setOnClickListener(view -> {
            if(sharedPrefs.getBooleanValue("isLoggedIn") &&
                    sharedPrefs.getValue("userType").equalsIgnoreCase("doctor")){
                //todo doctor's dash board
                startActivity(new Intent(HomeActivity.this, DoctorDashboardActivity.class));
                finish();
            }else{
                //todo doctor's pass userType == doctor so that we reuse the login and register pages
                startActivity(new Intent(HomeActivity.this, DoctorLoginActivity.class));
                finish();
            }
        });
        findViewById(R.id.btnPatient).setOnClickListener(view -> {
            if(sharedPrefs.getBooleanValue("isLoggedIn") &&
                    sharedPrefs.getValue("userType").equalsIgnoreCase("patient")){
                startActivity(new Intent(HomeActivity.this, PatientDashboardActivity.class));
                finish();
            }else{
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        });

        findViewById(R.id.tvForgotPasswordLink).setOnClickListener(v->{
            startActivity(new Intent(HomeActivity.this, ForgotPasswordActivity.class));
            finish();
        });
    }
}