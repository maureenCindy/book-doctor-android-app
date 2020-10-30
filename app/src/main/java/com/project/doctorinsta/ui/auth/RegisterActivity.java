package com.project.doctorinsta.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hbb20.CountryCodePicker;
import com.project.doctorinsta.R;
import com.project.doctorinsta.dao.PatientDao;
import com.project.doctorinsta.dao.UserDao;
import com.project.doctorinsta.data.Patient;
import com.project.doctorinsta.data.User;
import com.project.doctorinsta.database.DoctorInstaDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    MaterialDialog materialDialog;
    EditText etEmail, etFirstName, etLastname, etPassword, etAddress;
    String firstname, lastname, email, password, country, address;
    CountryCodePicker countryPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");
        etFirstName = findViewById(R.id.etFirstName);
        etLastname = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etAddress = findViewById(R.id.etAddress);
        countryPicker = findViewById(R.id.etCountry);

        //Sign Up Function
        Button signup = findViewById(R.id.btnRegister);
        signup.setOnClickListener(view -> {
            firstname = etFirstName.getText().toString();
            lastname = etLastname.getText().toString();
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();
            address = etAddress.getText().toString();
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
                materialDialog = new MaterialDialog.Builder(RegisterActivity.this)
                        .title("Creating profile")
                        .content("Please wait ...")
                        .progress(true, 0).show();
                UserDao userDao = DoctorInstaDatabase.getDatabase(this).userDao();
                PatientDao patientDao = DoctorInstaDatabase.getDatabase(this).patientDao();
                if(userDao.findByEmail(email)!=null){
                    if (materialDialog.isShowing()) {
                        materialDialog.dismiss();
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("Success")
                                .setCancelable(false)
                                .setMessage("Profile created successfully. Please login")
                                .setNegativeButton("Ok", (dialog, which) -> {
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                })
                                .show();
                    }
                }else{
                    User newUser = new User();
                    newUser.setAddress(address);
                    newUser.setCountry(country);
                    newUser.setPassword(password);
                    newUser.setEmail(email);
                    newUser.setFirstname(firstname);
                    newUser.setLastname(lastname);
                    Long id =userDao.save(newUser);
                    Patient patient = new Patient();
                    patient.setUserId(id);
                    patientDao.save(patient);
                    if (materialDialog.isShowing()) {
                        materialDialog.dismiss();
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("Success")
                                .setCancelable(false)
                                .setMessage("Profile created successfully. Please login")
                                .setNegativeButton("Ok", (dialog, which) -> {
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                })
                                .show();
                    }
                }
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
