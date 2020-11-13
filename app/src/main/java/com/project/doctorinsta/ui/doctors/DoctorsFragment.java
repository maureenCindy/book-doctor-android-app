package com.project.doctorinsta.ui.doctors;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.doctorinsta.R;
import com.project.doctorinsta.SharedPrefs;
import com.project.doctorinsta.adapter.DoctorAdapter;
import com.project.doctorinsta.adapter.SpecialityAdapter;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.Specialisation;
import com.project.doctorinsta.ui.specialisation.GridSpacingItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class DoctorsFragment extends Fragment {

    private DoctorAdapter doctorAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private SharedPrefs sharedPrefs;
    private Long selectedSpecialityNumber;
    private List<Doctor> doctors = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_doctors,container,false);
        sharedPrefs = SharedPrefs.getInstance(getActivity());
        selectedSpecialityNumber =sharedPrefs.getLongValue("selectedSpecialityNumber");
        Log.d("Specialty selected>>>", String.valueOf(selectedSpecialityNumber));
        recyclerView = root.findViewById(R.id.recyclerView);
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("doctors");
        Query specList = dbRef.orderByChild("specialtyIdNumber");
        specList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                Log.d("found doctors",":"+dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Long docSpecialty =snapshot.child("specialtyIdNumber").getValue(Long.class);
                    Log.d("Specialty selected ", selectedSpecialityNumber+"=="+docSpecialty);
                    if(docSpecialty.equals(selectedSpecialityNumber)){
                        Log.d("found doctor",":"+snapshot.toString());
                        String firstname =snapshot.child("firstname").getValue(String.class);
                        String lastname =snapshot.child("lastname").getValue(String.class);
                        Long idNumber =snapshot.child("idNumber").getValue(Long.class);
                        Long specialtyIdNumber =snapshot.child("specialtyIdNumber").getValue(Long.class);
                        String rate =snapshot.child("rate").getValue(String.class);
                        String experience =snapshot.child("experience").getValue(String.class);
                        String country =snapshot.child("country").getValue(String.class);
                        String city =snapshot.child("city").getValue(String.class);
                        String address =snapshot.child("address").getValue(String.class);
                        final Doctor doctor  = new Doctor( experience, idNumber, specialtyIdNumber,  rate,  firstname,  lastname,  "phone",
                                 "email",  "password",  country,  city,  address);
                        doctors.add(doctor);
                    }
                }
                linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                doctorAdapter = new DoctorAdapter(getActivity(), doctors);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 2, true));
                recyclerView.setAdapter(doctorAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "load doctors:onCancelled", databaseError.toException());
                // ...
            }
        });
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
