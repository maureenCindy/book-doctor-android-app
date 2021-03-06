package com.project.doctorinsta.patient_ui.specialisation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.doctorinsta.R;
import com.project.doctorinsta.adapter.SpecialityAdapter;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.Specialisation;
import com.project.doctorinsta.patient_ui.maps.MapsActivity;
import com.project.doctorinsta.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SpecialisationFragment extends Fragment {

    private TextView tvSearchByLocation;
    private SpecialityAdapter specialityAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private MaterialDialog materialDialog;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_specialisation,container,false);
        tvSearchByLocation = root.findViewById(R.id.tvSearchByLocation);
        tvSearchByLocation.setOnClickListener(view -> {
            startActivity(new Intent(view.getContext(), MapsActivity.class));
        });
        recyclerView = root.findViewById(R.id.recyclerView);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         materialDialog = new MaterialDialog.Builder(getActivity())
                 .title("loading data ")
                 .content("wait ...")
                 .progress(true, 0).show();
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("specialisations");
        Query specList = dbRef.orderByChild("id");
        specList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                List<Specialisation> specialisations = new ArrayList<>();
                Log.d("found specialisations",":"+dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Log.d("found specialisation",":"+snapshot.toString());
                    String nameFromDb =snapshot.child("name").getValue(String.class);
                    String descFromDb =snapshot.child("description").getValue(String.class);
                    Long idFromDb =snapshot.child("id").getValue(Long.class);
                    Specialisation specialisation = new Specialisation(idFromDb,nameFromDb,descFromDb);
                    specialisations.add(specialisation);
                }
                SharedPrefs sharedPrefs = SharedPrefs.getInstance(getActivity());
                sharedPrefs.setSpecialities("specialities",specialisations);
                Log.d("found specialisations",":"+specialisations.size()+" for adapter");
                //fetching all docs
                List<Doctor> doctors = new ArrayList<>();
                DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("doctors");
                Query docList = dbRef.orderByChild("phone");
                docList.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        Log.d("found doctors",":"+dataSnapshot.getChildrenCount());
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            Log.d("found doctor",":"+snapshot.toString());
                            String firstname =snapshot.child("firstname").getValue(String.class);
                            String lastname =snapshot.child("lastname").getValue(String.class);
                            Long idNumber =snapshot.child("idNumber").getValue(Long.class);
                            Long specialtyIdNumber =snapshot.child("specialtyIdNumber").getValue(Long.class);
                            String rate =snapshot.child("rate").getValue(String.class);
                            String experience =snapshot.child("experience").getValue(String.class);
                            String country =snapshot.child("country").getValue(String.class);
                            if(country!=null && country.contains("(")){
                                country= country.substring(0,country.indexOf("("));
                            }
                            String city =snapshot.child("city").getValue(String.class);
                            String address =snapshot.child("address").getValue(String.class);
                            if(address!=null && address.contains(",")){
                                address.replace(","," ");
                            }
                            final Doctor doctor  = new Doctor( experience, idNumber, specialtyIdNumber,  rate,  firstname,  lastname,  "phone",
                                    "email",  "password",  country,  city,  address);
                            doctors.add(doctor);
                        }
                        sharedPrefs.setAllDoctors("all-docs",doctors);
                        if(materialDialog.isShowing()){
                            materialDialog.dismiss();
                        }
                        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        specialityAdapter = new SpecialityAdapter(getActivity(), specialisations);
                        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 2, true));
                        recyclerView.setAdapter(specialityAdapter);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "load doctors:onCancelled", databaseError.toException());
                        // ...
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "load Specialisation:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
