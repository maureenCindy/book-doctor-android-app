package com.project.doctorinsta.patient_ui.booking;

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

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.doctorinsta.R;
import com.project.doctorinsta.adapter.BookingsAdapter;
import com.project.doctorinsta.data.BookingInfo;
import com.project.doctorinsta.data.Patient;
import com.project.doctorinsta.data.Schedule;
import com.project.doctorinsta.data.Specialisation;
import com.project.doctorinsta.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MyBookingsFragment extends Fragment {

    private BookingsAdapter bookingsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private SharedPrefs sharedPrefs;
    private MaterialDialog materialDialog;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public MyBookingsFragment() {
        // Required empty public constructor
    }

    private String mParam1;
    private String mParam2;

    public static MyBookingsFragment newInstance(String param1, String param2) {
        MyBookingsFragment fragment = new MyBookingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_patient_bookings, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPrefs = SharedPrefs.getInstance(getActivity());
        Patient patient = sharedPrefs.getPatient("loggedInPatient");
        if(patient!=null){
            materialDialog = new MaterialDialog.Builder(getActivity())
                    .title("Loading your bookings")
                    .content("Please wait ...")
                    .progress(true, 0).show();
            DatabaseReference dbBookingsRef = FirebaseDatabase.getInstance().getReference("bookings");
            Query query = dbBookingsRef.orderByChild("patientPhone").equalTo(patient.getPhone());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                    List<Long> scheduleIds = new ArrayList<>();
                    List<BookingInfo> bookings = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Long idFromDb = snapshot.child("scheduleId").getValue(Long.class);
                        scheduleIds.add(idFromDb);
                    }
                    DatabaseReference dbSchedulesRef = FirebaseDatabase.getInstance().getReference("schedules");
                    Query scheList = dbSchedulesRef.orderByChild("id");
                    scheList.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            List<Schedule> results = new ArrayList<>();
                            for (Long scheduleId : scheduleIds) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Long idFromDb = snapshot.child("id").getValue(Long.class);
                                    if (idFromDb != null && idFromDb.equals(scheduleId)) {
                                        Log.d("found user schedule", " {} " + snapshot.toString());
                                        String date = snapshot.child("date").getValue(String.class);
                                        String from = snapshot.child("startTime").getValue(String.class);
                                        String to = snapshot.child("endTime").getValue(String.class);
                                        String status = snapshot.child("status").getValue(String.class);
                                        Long docId = snapshot.child("doctorIdNumber").getValue(Long.class);
                                        Schedule schedule = new Schedule(idFromDb, docId, date, from, to, status);
                                        results.add(schedule);
                                    }
                                }
                            }
                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("doctors");
                            Query docList = dbRef.orderByChild("phone");
                            docList.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                    for (Schedule slot : results) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Long idNumberFromDb = snapshot.child("idNumber").getValue(Long.class);
                                            if (idNumberFromDb != null && idNumberFromDb.equals(slot.getDoctorIdNumber())) {
                                                Log.d("found doctor", ":" + snapshot.toString());
                                                String firstname = snapshot.child("firstname").getValue(String.class);
                                                String lastname = snapshot.child("lastname").getValue(String.class);
                                                Long specialtyIdNumber = snapshot.child("specialtyIdNumber").getValue(Long.class);
                                                String address = snapshot.child("address").getValue(String.class);
                                                String city = snapshot.child("city").getValue(String.class);
                                                String country = snapshot.child("country").getValue(String.class);
                                                String location = address+", "+city+", "+country;
                                                final BookingInfo booking = new BookingInfo("Dr " + firstname + "" + lastname,
                                                        getSpecialtyById(specialtyIdNumber),""+ slot.getDate(),
                                                        ""+slot.getStartTime(), slot.getEndTime(), location);
                                                bookings.add(booking);
                                            }
                                        }
                                    }
                                    if (materialDialog.isShowing()) {
                                        materialDialog.dismiss();
                                    }
                                    linearLayoutManager = new LinearLayoutManager(getActivity(),
                                            LinearLayoutManager.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    bookingsAdapter = new BookingsAdapter(getActivity(), bookings);
                                    recyclerView.setAdapter(bookingsAdapter);
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
                            Log.w(TAG, "load schedule:onCancelled", databaseError.toException());
                            // ...
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "load schedule:onCancelled", databaseError.toException());
                    // ...
                }
            });
        }


    }

    private String getSpecialtyById(Long specialityIdNumber) {
        for (Specialisation sp : sharedPrefs.getSpecialities("specialities")) {
            if (sp.getNumber().equals(specialityIdNumber)) {
                return sp.getName();
            }
        }
        return "invalid";
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
