package com.project.doctorinsta.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.project.doctorinsta.SharedPrefs;
import com.project.doctorinsta.adapter.SpecialityAdapter;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.Patient;
import com.project.doctorinsta.data.Specialisation;
import com.project.doctorinsta.ui.specialisation.GridSpacingItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class DoctorRegisterActivity extends AppCompatActivity  {
    MaterialDialog materialDialog;
    SmartMaterialSpinner specialtyDropDown;
    EditText etEmail,etPhone, etFirstName, etLastname, etPassword, etAddress, etCity, etRate, eExperience, etIdNumber;
    String firstname, lastname, email, phone, password, country, address, city, rate, experience, idNumber;
    CountryCodePicker countryPicker;
    List<Specialisation> specialisations = new ArrayList<>();
    String selectedSpecialty;
    int selectedPosition=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);
        getSupportActionBar().setTitle("Doctor Register");
        etFirstName = findViewById(R.id.etFirstName);
        etLastname = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etRate = findViewById(R.id.etRate);
        eExperience = findViewById(R.id.etExperience);
        etIdNumber = findViewById(R.id.etIdNumber);
        countryPicker = findViewById(R.id.etCountry);
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("specialisations");
        Query specList = dbRef.orderByChild("id");
        specList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                specialisations = new ArrayList<>();
                Log.d("found specialisations",":"+dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Log.d("found specialisation",":"+snapshot.toString());
                    String nameFromDb =snapshot.child("name").getValue(String.class);
                    String descFromDb =snapshot.child("description").getValue(String.class);
                    Long idFromDb =snapshot.child("id").getValue(Long.class);
                    Specialisation specialisation = new Specialisation(idFromDb,nameFromDb,descFromDb);
                    specialisations.add(specialisation);
                }
                String[] specialtiesList= new String[specialisations.size()];
                SharedPrefs sharedPrefs = SharedPrefs.getInstance(getApplicationContext());
                sharedPrefs.setSpecialities("specialities",specialisations);
                Log.d("Found specialisations",":"+specialisations.size()+" for adapter");
                specialtyDropDown = findViewById(R.id.spinnerSpeciality);
                for(int i=0; i< specialisations.size(); i++){
                    specialtiesList[i]=specialisations.get(i).getName();
                }
                specialtyDropDown.setItem(Arrays.asList(specialtiesList));
                selectedSpecialty= specialtiesList[selectedPosition];
                specialtyDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                        selectedSpecialty =specialtiesList[pos];
                        selectedPosition =pos;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                //Sign Up Function
                Button signup = findViewById(R.id.btnRegister);
                signup.setOnClickListener(view -> {
                    firstname = etFirstName.getText().toString();
                    lastname = etLastname.getText().toString();
                    email = etEmail.getText().toString();
                    phone = etPhone.getText().toString();
                    password = etPassword.getText().toString();
                    address = etAddress.getText().toString();
                    city = etCity.getText().toString();
                    rate = etRate.getText().toString();
                    experience = eExperience.getText().toString();
                    idNumber = etIdNumber.getText().toString();
                    country = countryPicker.getSelectedCountryEnglishName();
                    if (!isValidName(firstname) || !isValidName(lastname)) {
                        etFirstName.setError("Invalid name");
                    } else if (!isValidEmail(email)) {
                        etEmail.setError("Invalid Email");
                    } else if (!isValidPassword(password)) {
                        etPassword.setError("Invalid password");
                    } else if (!isValidName(address)) {
                        etAddress.setError("Invalid address");
                    } else {
                        materialDialog = new MaterialDialog.Builder(DoctorRegisterActivity.this)
                                .title("Creating profile")
                                .content("Please wait ...")
                                .progress(true, 0)
                                .show();
                        //   saving data;
                        DatabaseReference dbUserRef = FirebaseDatabase.getInstance().getReference("doctors");
                        //todo check if user phone exists
                        Doctor doctor = new Doctor(experience, Long.valueOf(idNumber), specialisations.get(selectedPosition).getNumber(),
                                rate, firstname, lastname, phone, email, password, country, city, address);
                        dbUserRef.child(phone).setValue(doctor);
                        if (materialDialog.isShowing()) {
                            materialDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DoctorRegisterActivity.this, DoctorLoginActivity.class));
                        finish();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "load Specialisation:onCancelled", databaseError.toException());
                // ...
            }
        });

    }

    // validating user id
    private boolean isValidName(String user) {
        return user != null && user.length() > 0;
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        return pass != null && pass.length() > 0;
    }


}