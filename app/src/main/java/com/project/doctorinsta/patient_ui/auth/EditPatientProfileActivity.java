package com.project.doctorinsta.patient_ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.project.doctorinsta.R;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.Patient;
import com.project.doctorinsta.doctor_ui.dashboard.DoctorDashboardActivity;
import com.project.doctorinsta.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditPatientProfileActivity extends AppCompatActivity  {
    MaterialDialog materialDialog;
    EditText etPhone, etFirstName, etLastname, etAddress, etCity, etRate, eExperience;
    String firstname, lastname, phone, country, address, city, rate, experience;
    CountryCodePicker countryPicker;
    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_edit_profile);
        getSupportActionBar().setTitle("Patient Profile");
        SharedPrefs sharedPrefs = SharedPrefs.getInstance(EditPatientProfileActivity.this);
        patient =sharedPrefs.getPatient("loggedInPatient");
        etFirstName = findViewById(R.id.etFirstName);
        etFirstName.setText(patient.getFirstname());
        etLastname = findViewById(R.id.etLastName);
        etLastname.setText(patient.getLastname());
        etPhone = findViewById(R.id.etPhone);
        etPhone.setText(patient.getPhone());
        etAddress = findViewById(R.id.etAddress);
        etAddress.setText(patient.getAddress());
        etCity = findViewById(R.id.etCity);
        etCity.setText(patient.getCity());
        countryPicker = findViewById(R.id.etCountry);
                //Sign Up Function
                Button updateProfile = findViewById(R.id.btnUpdateProfile);
                updateProfile.setOnClickListener(view -> {
                    firstname = etFirstName.getText().toString();
                    lastname = etLastname.getText().toString();
                    phone = etPhone.getText().toString();
                    address = etAddress.getText().toString();
                    city = etCity.getText().toString();
                    country = countryPicker.getSelectedCountryEnglishName();
                    if (!isValidName(firstname) || !isValidName(lastname)) {
                        etFirstName.setError("Invalid name");
                    }  else {
                        materialDialog = new MaterialDialog.Builder(EditPatientProfileActivity.this)
                                .title("Updating profile")
                                .content("Please wait ...")
                                .progress(true, 0)
                                .show();
                        //todo update profile
                        Log.d("Searching for patient"," with email, "+patient.getEmail());
                        DatabaseReference dbSchedulesRef = FirebaseDatabase.getInstance().getReference("patients");
                        Query query = dbSchedulesRef.orderByChild("phone").equalTo(patient.getPhone());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if(snapshot.exists() ){
                                    String email =snapshot.child(String.valueOf(patient.getPhone())).child("email").getValue(String.class);
                                    if(email!=null && email.equalsIgnoreCase(patient.getEmail())){
                                        Log.d("found doc"," {}, "+snapshot.toString());
                                        snapshot.child(String.valueOf(patient.getPhone())).child("firstname").getRef().setValue(firstname);
                                        snapshot.child(String.valueOf(patient.getPhone())).child("lastname").getRef().setValue(lastname);
                                        snapshot.child(String.valueOf(patient.getPhone())).child("phone").getRef().setValue(phone);
                                        snapshot.child(String.valueOf(patient.getPhone())).child("address").getRef().setValue(address);
                                        snapshot.child(String.valueOf(patient.getPhone())).child("city").getRef().setValue(city);
                                        snapshot.child(String.valueOf(patient.getPhone())).child("country").getRef().setValue(country);
                                        if (materialDialog.isShowing()) {
                                            materialDialog.dismiss();
                                        }
                                        Toast.makeText(EditPatientProfileActivity.this,"Profile updated successfully.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(EditPatientProfileActivity.this, DoctorDashboardActivity.class);
                                        startActivity(intent);
                                    }
                                }else {
                                    Log.d("Could not find patient"," with id "+patient.getEmail());
                                    if (materialDialog.isShowing()) {
                                        materialDialog.dismiss();
                                    }
                                    Toast.makeText(EditPatientProfileActivity.this,"Error contact support", Toast.LENGTH_LONG).show();
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