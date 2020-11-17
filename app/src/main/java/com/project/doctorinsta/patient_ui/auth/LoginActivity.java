package com.project.doctorinsta.patient_ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.doctorinsta.R;
import com.project.doctorinsta.data.Patient;
import com.project.doctorinsta.patient_ui.dashboard.PatientDashboardActivity;
import com.project.doctorinsta.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {
    MaterialDialog materialDialog;
    EditText username, password;
    SharedPrefs sharedPrefs;
    TextView register;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs =SharedPrefs.getInstance(this);
        getSupportActionBar().setTitle("Login");
        firebaseAuth= FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
        register=findViewById(R.id.tvRegister);
        findViewById(R.id.btnLogin).setOnClickListener(view -> {
            String email = username.getText().toString().trim();
            final String pass = password.getText().toString().trim();
            if (!isValidUsername(email)) {
                username.setError("Invalid username");
            } else if (!isValidPassword(pass)) {
                password.setError("Invalid Password");
            } else {
                //loginUser(email, pass);
                login(email,pass);
            }
        });
        register.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));
    }


    private void login(String username, String password){
        materialDialog = new MaterialDialog.Builder(LoginActivity.this)
                .title("authenticating patient")
                .content("wait ...")
                .progress(true, 0).show();
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("patients");
        Query checkUser = db.orderByChild("phone").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String storedPass = snapshot.child(username).child("password").getValue(String.class);
                    if(storedPass.equals(password)){
                        String nameFromDb =snapshot.child(username).child("firstname").getValue(String.class);
                        String surnameFromDb =snapshot.child(username).child("lastname").getValue(String.class);
                        String emailFromDb =snapshot.child(username).child("email").getValue(String.class);
                        String addressFromDb =snapshot.child(username).child("address").getValue(String.class);
                        String countryFromDb =snapshot.child(username).child("country").getValue(String.class);
                        String cityFromDb =snapshot.child(username).child("city").getValue(String.class);
                        String phoneFromDb =snapshot.child(username).child("phone").getValue(String.class);
                        Patient patient = new Patient( phoneFromDb,  emailFromDb,  "password",  nameFromDb,  surnameFromDb,
                                 addressFromDb,  countryFromDb,  cityFromDb);
                        sharedPrefs.setPatient("loggedInPatient",patient);
                        sharedPrefs.setBooleanValue("isLoggedIn",true);
                        sharedPrefs.setValue("userType","patient");
                        if (materialDialog.isShowing()) {
                            materialDialog.dismiss();
                        }
                        startActivity(new Intent(LoginActivity.this, PatientDashboardActivity.class));
                        finish();
                    }else {
                        if (materialDialog.isShowing()) {
                            materialDialog.dismiss();
                        }
                        new MaterialDialog.Builder(LoginActivity.this)
                                .title("Login Error")
                                .content("Wrong username or password")
                                .positiveText("OK")
                                .show();
                    }
                }else{
                    if (materialDialog.isShowing()) {
                        materialDialog.dismiss();
                    }
                    new MaterialDialog.Builder(LoginActivity.this)
                            .title("Login Error")
                            .content("Wrong username or password")
                            .positiveText("OK")
                            .show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                if (materialDialog.isShowing()) {
                    materialDialog.dismiss();
                }
                new MaterialDialog.Builder(LoginActivity.this)
                        .title("Login Error")
                        .content(error.getMessage())
                        .positiveText("OK")
                        .show();
            }
        });
    }

    // validating username field
    private boolean isValidUsername (String username){
        return username != null && username.length() > 0;
    }

    // validating password with retype password
    private boolean isValidPassword (String pass){
        return pass != null && pass.length() > 0;
    }
}