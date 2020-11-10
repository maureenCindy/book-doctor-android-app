package com.project.doctorinsta.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.project.doctorinsta.PatientDashboardActivity;
import com.project.doctorinsta.R;
import com.project.doctorinsta.data.Patient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    MaterialDialog materialDialog;
    EditText etEmail,etPhone, etFirstName, etLastname, etPassword, etAddress, etCity;
    String firstname, lastname, email, phone, password, country, address, city;
    CountryCodePicker countryPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");
        etFirstName = findViewById(R.id.etFirstName);
        etLastname = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        countryPicker = findViewById(R.id.etCountry);
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
                //   saving data;
                DatabaseReference dbUserRef = FirebaseDatabase.getInstance().getReference("patients");
                //todo check if user phone exists
                Patient user = new Patient(phone, email, password, firstname, lastname, address, country, city);
                dbUserRef.child(phone).setValue(user);
                if (materialDialog.isShowing()) {
                    materialDialog.dismiss();
                }
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
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