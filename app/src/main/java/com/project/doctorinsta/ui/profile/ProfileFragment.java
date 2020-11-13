package com.project.doctorinsta.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.project.doctorinsta.R;
import com.project.doctorinsta.SharedPrefs;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.Patient;
import com.project.doctorinsta.ui.auth.LoginActivity;
import com.project.doctorinsta.ui.home.HomeActivity;


public class ProfileFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile,container,false);
        TextView fullname= root.findViewById(R.id.fullNameTextView);
        TextView email= root.findViewById(R.id.emailAddressTextView);
        SharedPrefs sharedPrefs = SharedPrefs.getInstance(getActivity());
        if(sharedPrefs.getValue("userType").equalsIgnoreCase("doctor")){
            Doctor doctor = sharedPrefs.getDoctor("loggedInDoctor");
            fullname.setText("Dr "+doctor.getFirstname()+ " "+ doctor.getLastname());
            email.setText(doctor.getPhone());
        }else {
            Patient patient = sharedPrefs.getPatient("loggedInPatient");
            fullname.setText(patient.getFirstname()+ " "+ patient.getLastname());
            email.setText(patient.getPhone());
        }
        root.findViewById(R.id.logoutTextView).setOnClickListener(v->{
            sharedPrefs.clearAllPreferences();
            startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}