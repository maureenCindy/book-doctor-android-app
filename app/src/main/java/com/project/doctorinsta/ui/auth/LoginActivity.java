package com.project.doctorinsta.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.project.doctorinsta.PatientDashboardActivity;
import com.project.doctorinsta.R;
import com.project.doctorinsta.SharedPrefs;
import com.project.doctorinsta.dao.DoctorDao;
import com.project.doctorinsta.dao.DoctorScheduleDao;
import com.project.doctorinsta.dao.SpecialisationDao;
import com.project.doctorinsta.dao.UserDao;
import com.project.doctorinsta.data.User;
import com.project.doctorinsta.database.DoctorInstaDatabase;
import com.project.doctorinsta.database.InitialisationData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    MaterialDialog materialDialog;
    public static final String PREFS = "user_details";
    EditText username, password;
    SharedPrefs sharedPrefs;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs =SharedPrefs.getInstance(this);
        initialiseDatabase();
        Toolbar toolbar= findViewById(R.id.toolbar);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        register=findViewById(R.id.tvRegister);
        findViewById(R.id.btnLogin).setOnClickListener(view -> {
            String email = username.getText().toString().trim();
            final String pass = password.getText().toString().trim();
            if (!isValidEmail(email)) {
                username.setError("Invalid email address");
            } else if (!isValidPassword(pass)) {
                password.setError("Invalid Password");
            } else {
                loginUser(email, pass);
            }
        });
        register.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));

    }

    private void initialiseDatabase() {
        SpecialisationDao specialisationDao =DoctorInstaDatabase.getDatabase(this).specialisationDao();
        UserDao userDao = DoctorInstaDatabase.getDatabase(this).userDao();
        DoctorDao doctorDao =DoctorInstaDatabase.getDatabase(this).doctorDao();
        DoctorScheduleDao scheduleDao = DoctorInstaDatabase.getDatabase(this).doctorScheduleDao();
        if(specialisationDao.findAll().size()==0){
            specialisationDao.insertAll(InitialisationData.populateSpecialities());
            userDao.insertAll(InitialisationData.populateUsers());
            doctorDao.insertAll(InitialisationData.populateDoctors());
            scheduleDao.insertAll(InitialisationData.populateDoctorSchedule());
        }
    }

    // validating email id
    private boolean isValidEmail (String email){
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void loginUser(String username, String password) {
        materialDialog = new MaterialDialog.Builder(LoginActivity.this)
                .title("Authenticating")
                .content("Please wait ...")
                .progress(true, 0).show();
        UserDao userDao = DoctorInstaDatabase.getDatabase(this).userDao();
        if (userDao.findByEmailAndPass(username,password)!=null) {
            if (materialDialog.isShowing()) {
                materialDialog.dismiss();
            }
            User user =userDao.findByEmailAndPass(username,password);
            sharedPrefs.setLongValue("userId", user.getId());
            sharedPrefs.setValue("userType", user.getUserType());
            sharedPrefs.setValue("fullname", user.getFirstname()+ " "+ user.getLastname());
            sharedPrefs.setValue("email", user.getEmail());
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
    }
    // validating password with retype password
    private boolean isValidPassword (String pass){
        return pass != null && pass.length() > 0;
    }
}