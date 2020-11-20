package com.project.doctorinsta.doctor_ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.project.doctorinsta.R;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.Specialisation;
import com.project.doctorinsta.doctor_ui.dashboard.DoctorDashboardActivity;
import com.project.doctorinsta.patient_ui.dashboard.PatientDashboardActivity;
import com.project.doctorinsta.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class EditDoctorProfileActivity extends AppCompatActivity  {
    MaterialDialog materialDialog;
    EditText etPhone, etFirstName, etLastname, etAddress, etCity, etRate, eExperience;
    String firstname, lastname, phone, country, address, city, rate, experience;
    CountryCodePicker countryPicker;
    Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_edit_profile);
        getSupportActionBar().setTitle("Doctor Profile");
        SharedPrefs sharedPrefs = SharedPrefs.getInstance(EditDoctorProfileActivity.this);
        doctor =sharedPrefs.getDoctor("loggedInDoctor");
        etFirstName = findViewById(R.id.etFirstName);
        etFirstName.setText(doctor.getFirstname());
        etLastname = findViewById(R.id.etLastName);
        etLastname.setText(doctor.getLastname());
        etPhone = findViewById(R.id.etPhone);
        etPhone.setText(doctor.getPhone());
        etAddress = findViewById(R.id.etAddress);
        etAddress.setText(doctor.getAddress());
        etCity = findViewById(R.id.etCity);
        etCity.setText(doctor.getCity());
        etRate = findViewById(R.id.etRate);
        etRate.setText(doctor.getRate());
        eExperience = findViewById(R.id.etExperience);
        eExperience.setText(doctor.getExperience());
        countryPicker = findViewById(R.id.etCountry);
                //Sign Up Function
                Button updateProfile = findViewById(R.id.btnUpdateProfile);
                updateProfile.setOnClickListener(view -> {
                    firstname = etFirstName.getText().toString();
                    lastname = etLastname.getText().toString();
                    phone = etPhone.getText().toString();
                    address = etAddress.getText().toString();
                    city = etCity.getText().toString();
                    rate = etRate.getText().toString();
                    experience = eExperience.getText().toString();
                    country = countryPicker.getSelectedCountryEnglishName();
                    if (!isValidName(firstname) || !isValidName(lastname)) {
                        etFirstName.setError("Invalid name");
                    }  else {
                        materialDialog = new MaterialDialog.Builder(EditDoctorProfileActivity.this)
                                .title("Updating profile")
                                .content("Please wait ...")
                                .progress(true, 0)
                                .show();
                        //todo update profile
                        Log.d("Searching for doctor"," with IdNumber, "+doctor.getIdNumber());
                        DatabaseReference dbSchedulesRef = FirebaseDatabase.getInstance().getReference("doctors");
                        Query query = dbSchedulesRef.orderByChild("phone").equalTo(doctor.getPhone());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if(snapshot.exists() ){
                                    Log.d("found doc"," {}, "+snapshot.toString());
                                    snapshot.child(String.valueOf(doctor.getPhone())).child("firstname").getRef().setValue(firstname);
                                    snapshot.child(String.valueOf(doctor.getPhone())).child("lastname").getRef().setValue(lastname);
                                    snapshot.child(String.valueOf(doctor.getPhone())).child("phone").getRef().setValue(phone);
                                    snapshot.child(String.valueOf(doctor.getPhone())).child("address").getRef().setValue(address);
                                    snapshot.child(String.valueOf(doctor.getPhone())).child("city").getRef().setValue(city);
                                    snapshot.child(String.valueOf(doctor.getPhone())).child("rate").getRef().setValue(rate);
                                    snapshot.child(String.valueOf(doctor.getPhone())).child("experience").getRef().setValue(experience);
                                    snapshot.child(String.valueOf(doctor.getPhone())).child("country").getRef().setValue(country);
                                    if (materialDialog.isShowing()) {
                                        materialDialog.dismiss();
                                    }
                                    Toast.makeText(EditDoctorProfileActivity.this,"Profile updated successfully.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(EditDoctorProfileActivity.this, DoctorDashboardActivity.class);
                                    startActivity(intent);

                                }else {
                                    Log.d("Could not find doctor"," with id "+doctor.getIdNumber());
                                    if (materialDialog.isShowing()) {
                                        materialDialog.dismiss();
                                    }
                                    Toast.makeText(EditDoctorProfileActivity.this,"Error contact support", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            }
                        });
                    }
        });
    }

    // validating user id
    private boolean isValidName(String user) {
        return user != null && user.length() > 0;
    }


}