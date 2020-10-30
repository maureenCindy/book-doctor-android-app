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
import com.project.doctorinsta.dao.UserDao;
import com.project.doctorinsta.data.User;
import com.project.doctorinsta.database.DoctorInstaDatabase;
import com.project.doctorinsta.ui.auth.LoginActivity;


public class ProfileFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile,container,false);
        TextView fullname= root.findViewById(R.id.fullNameTextView);
        TextView email= root.findViewById(R.id.emailAddressTextView);
        SharedPrefs sharedPrefs = SharedPrefs.getInstance(getActivity());
        UserDao userDao = DoctorInstaDatabase.getDatabase(getActivity()).userDao();
        User user =userDao.findByEmail(sharedPrefs.getValue("email"));
        fullname.setText(user.getFirstname()+" "+user.getLastname());
        email.setText(user.getEmail());
        root.findViewById(R.id.logoutTextView).setOnClickListener(v->{
            sharedPrefs.clearAllPreferences();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}