package com.project.doctorinsta.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import com.project.doctorinsta.PatientDashboardActivity;
import com.project.doctorinsta.R;
import com.project.doctorinsta.SharedPrefs;
import com.project.doctorinsta.ui.auth.DoctorLoginActivity;
import com.project.doctorinsta.ui.auth.LoginActivity;
import com.project.doctorinsta.ui.auth.RegisterActivity;

public class HomeActivity extends AppCompatActivity {

    private SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPrefs = SharedPrefs.getInstance(getApplicationContext());
        findViewById(R.id.btnDoctor).setOnClickListener(view -> {
            if(sharedPrefs.getBooleanValue("isLoggedIn") &&
                    sharedPrefs.getValue("userType").equalsIgnoreCase("doctor")){
                //todo doctor's dash board
                Toast.makeText(getApplicationContext(), "Doctor's dash page", Toast.LENGTH_LONG).show();
            }else{
                //todo doctor's pass userType == doctor so that we reuse the login and register pages
                startActivity(new Intent(getApplicationContext(), DoctorLoginActivity.class));
                finish();
            }
        });
        findViewById(R.id.btnPatient).setOnClickListener(view -> {
            if(sharedPrefs.getBooleanValue("isLoggedIn") &&
                    sharedPrefs.getValue("userType").equalsIgnoreCase("patient")){
                startActivity(new Intent(getApplicationContext(), PatientDashboardActivity.class));
                finish();
            }else{
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }
}