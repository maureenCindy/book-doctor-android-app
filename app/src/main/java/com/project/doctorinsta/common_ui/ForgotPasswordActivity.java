package com.project.doctorinsta.common_ui;

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
import com.project.doctorinsta.R;
import com.project.doctorinsta.utils.EmailClient;
import com.project.doctorinsta.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static com.project.doctorinsta.utils.SharedPrefs.sharedPrefs;


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    MaterialDialog materialDialog;
    EditText etUsername;
    SmartMaterialSpinner userTypeSpinnerDropdown;
    Button btnSubmit;
    String selectedUser;
    String[] users = {"Doctor", "Patient"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        userTypeSpinnerDropdown = findViewById(R.id.userTypeSpinnerDropdown);
        userTypeSpinnerDropdown.setItem(Arrays.asList(users));
        userTypeSpinnerDropdown.setOnItemSelectedListener(this);
        etUsername= findViewById(R.id.etUsername);
        btnSubmit= findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(etUsername.getText() == null || etUsername.getText().toString().isEmpty()){
            Toast.makeText(view.getContext(), "Email address is required!!", Toast.LENGTH_SHORT).show();
        }else if (selectedUser==null){
            Toast.makeText(view.getContext(), "user type is required!!", Toast.LENGTH_SHORT).show();
        }else{
            String email = etUsername.getText().toString();
                materialDialog = new MaterialDialog.Builder(view.getContext())
                        .title("Forgot password")
                        .content("sending password reset to your email address...")
                        .progress(true, 0)
                        .show();
                if(selectedUser.equalsIgnoreCase("doctor")){
                    //update doctor 's password
                    Log.d("Searching for doctor"," with email, "+email);
                    DatabaseReference db= FirebaseDatabase.getInstance().getReference("doctors");
                    Query checkUser = db.orderByChild("phone");
                    checkUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for(DataSnapshot childSnapshot: snapshot.getChildren()){
                                String storedPass = childSnapshot.child("password").getValue(String.class);
                                String storedEmail = childSnapshot.child("email").getValue(String.class);
                                if(storedEmail!=null && storedPass!=null &&
                                        storedEmail.equalsIgnoreCase(email)){
                                    //todo send email to user with password
                                    new Thread(() -> {
                                        try {
                                            EmailClient emailClient = new EmailClient();
                                            emailClient.send(email, "Doctor",storedPass);
                                        } catch (Exception e) {
                                            Log.e("SendMail", e.getMessage(), e);
                                        }
                                    }).start();
                                    SharedPrefs sharedPrefs = SharedPrefs.getInstance(view.getContext());
                                    sharedPrefs.setBooleanValue("found", true);
                                    if (materialDialog.isShowing()) {
                                        materialDialog.dismiss();
                                    }
                                    Toast.makeText(view.getContext(), "check your inbox.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(view.getContext(), HomeActivity.class));
                                }
                            }
                            if (materialDialog.isShowing()) {
                                materialDialog.dismiss();
                            }
                            if(!sharedPrefs.getBooleanValue("found")) {
                                Toast.makeText(view.getContext(), "Email is not registered", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }else if(selectedUser.equalsIgnoreCase("patient")){
                    //update patient 's password
                    Log.d("Searching for patient"," with email, "+email);
                    DatabaseReference db= FirebaseDatabase.getInstance().getReference("patients");
                    Query checkUser = db.orderByChild("phone");
                    checkUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for(DataSnapshot childSnapshot: snapshot.getChildren()){
                                String storedPass = childSnapshot.child("password").getValue(String.class);
                                String storedEmail = childSnapshot.child("email").getValue(String.class);
                                if(storedEmail!=null && storedPass!=null &&
                                        storedEmail.equalsIgnoreCase(email)){
                                    //todo send email to user with password
                                    new Thread(() -> {
                                        try {
                                            EmailClient emailClient = new EmailClient();
                                            emailClient.send(email, "Patient",storedPass);
                                        } catch (Exception e) {
                                            Log.e("SendMail", e.getMessage(), e);
                                        }
                                    }).start();
                                    SharedPrefs sharedPrefs = SharedPrefs.getInstance(view.getContext());
                                    sharedPrefs.setBooleanValue("found", true);
                                    if (materialDialog.isShowing()) {
                                        materialDialog.dismiss();
                                    }
                                    Toast.makeText(view.getContext(), "check your inbox.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(view.getContext(), HomeActivity.class));
                                }
                            }
                            if (materialDialog.isShowing()) {
                                materialDialog.dismiss();
                            }
                            if(!sharedPrefs.getBooleanValue("found")) {
                                Toast.makeText(view.getContext(), "Email is not registered", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }
        }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        selectedUser =users[pos];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        selectedUser =null;
    }
}