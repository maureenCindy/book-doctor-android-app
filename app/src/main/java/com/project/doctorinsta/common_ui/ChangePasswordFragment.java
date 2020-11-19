package com.project.doctorinsta.common_ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.doctorinsta.R;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.Patient;
import com.project.doctorinsta.doctor_ui.dashboard.DoctorDashboardActivity;
import com.project.doctorinsta.patient_ui.dashboard.PatientDashboardActivity;
import com.project.doctorinsta.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;


public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    MaterialDialog materialDialog;
    String newPass;
    String confirmPass;
    TextView etNewPassword, etConfirmPassword, btnSubmit;
    Doctor doctor;
    Patient patient;

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change_password,container,false);
        etNewPassword= root.findViewById(R.id.editTextNewPassword);
        etConfirmPassword= root.findViewById(R.id.editTextConfirmPassword);
        btnSubmit= root.findViewById(R.id.btnChangePassword);
        btnSubmit.setOnClickListener(this);
        SharedPrefs sharedPrefs = SharedPrefs.getInstance(getActivity());
        if(sharedPrefs.getValue("userType")!=null &&
                sharedPrefs.getValue("userType").equalsIgnoreCase("doctor")){
             doctor = sharedPrefs.getDoctor("loggedInDoctor");

        }else if(sharedPrefs.getValue("userType")!=null &&
                sharedPrefs.getValue("userType").equalsIgnoreCase("patient")){
             patient = sharedPrefs.getPatient("loggedInPatient");

        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        if(etNewPassword.getText() == null || etNewPassword.getText().toString().isEmpty()
                || etConfirmPassword.getText()==null || etConfirmPassword.getText().toString().isEmpty()){
            Toast.makeText(view.getContext(), "All fields are required!!", Toast.LENGTH_SHORT).show();
        }else{
            newPass = etNewPassword.getText().toString();
            confirmPass = etConfirmPassword.getText().toString();
            if(!newPass.equals(confirmPass)){
                Toast.makeText(view.getContext(), "Passwords must match!!", Toast.LENGTH_SHORT).show();
            }else{
                materialDialog = new MaterialDialog.Builder(view.getContext())
                        .title("Change password")
                        .content("updating password...")
                        .progress(true, 0)
                        .show();
                if(doctor!=null){
                    //update doctor 's password
                    Log.d("Searching for doctor"," with phone, "+doctor.getPhone());
                    DatabaseReference dbSchedulesRef = FirebaseDatabase.getInstance().getReference("doctors");
                    Query query = dbSchedulesRef.orderByChild("phone").equalTo(doctor.getPhone());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if(snapshot.exists() ){
                                snapshot.child(String.valueOf(doctor.getPhone())).child("password").getRef().setValue(newPass);
                                if (materialDialog.isShowing()) {
                                    materialDialog.dismiss();
                                }
                                startActivity(new Intent(view.getContext(), DoctorDashboardActivity.class));
                            }else {
                                Log.d("Not found doctor"," with phone, "+doctor.getPhone());
                                Toast.makeText(view.getContext(), "Failed to update password.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }else if(patient!=null){
                    //update patient 's password
                    Log.d("Searching for patient"," with phone, "+patient.getPhone());
                    DatabaseReference dbSchedulesRef = FirebaseDatabase.getInstance().getReference("patients");
                    Query query = dbSchedulesRef.orderByChild("phone").equalTo(patient.getPhone());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if(snapshot.exists() ){
                                snapshot.child(String.valueOf(patient.getPhone())).child("password").getRef().setValue(newPass);
                                if (materialDialog.isShowing()) {
                                    materialDialog.dismiss();
                                }
                                startActivity(new Intent(view.getContext(), PatientDashboardActivity.class));
                            }else {
                                Log.d("Not found patient"," with phone, "+patient.getPhone());
                                Toast.makeText(view.getContext(), "Failed to update password.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }
        }
    }
}