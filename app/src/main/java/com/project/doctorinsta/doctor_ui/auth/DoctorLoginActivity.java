package com.project.doctorinsta.doctor_ui.auth;

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
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.doctor_ui.dashboard.DoctorDashboardActivity;
import com.project.doctorinsta.patient_ui.auth.LoginActivity;
import com.project.doctorinsta.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

public class DoctorLoginActivity extends AppCompatActivity {
    MaterialDialog materialDialog;
    EditText username, password;
    SharedPrefs sharedPrefs;
    TextView register;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs =SharedPrefs.getInstance(this);
        getSupportActionBar().setTitle("Doctor Login");
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
        register.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), DoctorRegisterActivity.class)));
    }


    private void login(String username, String password){
        materialDialog = new MaterialDialog.Builder(DoctorLoginActivity.this)
                .title("authenticating doctor")
                .content("wait ...")
                .progress(true, 0).show();
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("doctors");
        Query checkUser = db.orderByChild("phone");
        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot: snapshot.getChildren()){
                    String storedPass = childSnapshot.child("password").getValue(String.class);
                    String storedEmail = childSnapshot.child("email").getValue(String.class);
                    if(storedEmail!=null && storedPass!=null &&
                            storedPass.equals(password) && storedEmail.equalsIgnoreCase(username)){
                        String nameFromDb =childSnapshot.child("firstname").getValue(String.class);
                        String surnameFromDb =childSnapshot.child("lastname").getValue(String.class);
                        String emailFromDb =childSnapshot.child("email").getValue(String.class);
                        String addressFromDb =childSnapshot.child("address").getValue(String.class);
                        String countryFromDb =childSnapshot.child("country").getValue(String.class);
                        String cityFromDb =childSnapshot.child("city").getValue(String.class);
                        String phoneFromDb =childSnapshot.child("phone").getValue(String.class);
                        String passFromDb =childSnapshot.child("password").getValue(String.class);
                        Long idNumberFromDb =childSnapshot.child("idNumber").getValue(Long.class);
                        Long specialtyIdNumberFromDb =childSnapshot.child("specialtyIdNumber").getValue(Long.class);
                        String rateFromDb =childSnapshot.child("rate").getValue(String.class);
                        String experienceFromDb =childSnapshot.child("experience").getValue(String.class);
                        Doctor doctor = new Doctor(experienceFromDb, idNumberFromDb, specialtyIdNumberFromDb,
                                rateFromDb, nameFromDb, surnameFromDb, phoneFromDb,
                                emailFromDb, passFromDb,countryFromDb, cityFromDb, addressFromDb);
                        sharedPrefs.setDoctor("loggedInDoctor",doctor);
                        sharedPrefs.setBooleanValue("isLoggedIn",true);
                        sharedPrefs.setValue("userType","doctor");
                        if (materialDialog.isShowing()) {
                            materialDialog.dismiss();
                        }
                        startActivity(new Intent(DoctorLoginActivity.this, DoctorDashboardActivity.class));
                    }
                }
                if (materialDialog.isShowing()) {
                    materialDialog.dismiss();
                }
                if(!sharedPrefs.getBooleanValue("isLoggedIn")){
                    new MaterialDialog.Builder(DoctorLoginActivity.this)
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
                new MaterialDialog.Builder(DoctorLoginActivity.this)
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