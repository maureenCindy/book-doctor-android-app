package com.project.doctorinsta.common_ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.project.doctorinsta.R;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.Patient;
import com.project.doctorinsta.doctor_ui.auth.EditDoctorProfileActivity;
import com.project.doctorinsta.patient_ui.auth.EditPatientProfileActivity;
import com.project.doctorinsta.patient_ui.dashboard.PatientDashboardActivity;
import com.project.doctorinsta.utils.SharedPrefs;


public class ProfileFragment extends Fragment {

    Patient patient;
    Doctor doctor;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile,container,false);
        TextView fullname= root.findViewById(R.id.fullNameTextView);
        TextView email= root.findViewById(R.id.emailAddressTextView);
        SharedPrefs sharedPrefs = SharedPrefs.getInstance(getActivity());
        if(sharedPrefs.getValue("userType")!=null &&
                sharedPrefs.getValue("userType").equalsIgnoreCase("doctor")){
            doctor = sharedPrefs.getDoctor("loggedInDoctor");
            if(doctor!=null){
                fullname.setText("Dr "+doctor.getFirstname()+ " "+ doctor.getLastname());
                email.setText(doctor.getEmail());
            }

        }else {
            patient = sharedPrefs.getPatient("loggedInPatient");
            if(patient!=null){
                fullname.setText(patient.getFirstname()+ " "+ patient.getLastname());
                email.setText(patient.getEmail());
            }

        }
        TextView editPassword =root.findViewById(R.id.changePasswordTextView);
        editPassword.setOnClickListener(v->{
            if(doctor!=null){
                Intent intent = new Intent(v.getContext(), PatientDashboardActivity.class);
                intent.putExtra("fragmentName", "ChangePassword");
                v.getContext().startActivity(intent);
            }else if(patient!=null){
                Intent intent = new Intent(v.getContext(), PatientDashboardActivity.class);
                intent.putExtra("fragmentName", "ChangePassword");
                v.getContext().startActivity(intent);
            }
        });

        root.findViewById(R.id.logoutTextView).setOnClickListener(v->{
            sharedPrefs.clearAllPreferences();
            startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();
        });

        root.findViewById(R.id.editProfileTextView).setOnClickListener(v->{
            if(doctor!=null){
                startActivity(new Intent(getActivity(), EditDoctorProfileActivity.class));
                getActivity().finish();
            }else if(patient!=null){
                startActivity(new Intent(getActivity(), EditPatientProfileActivity.class));
                getActivity().finish();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}