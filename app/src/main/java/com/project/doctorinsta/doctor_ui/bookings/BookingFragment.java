package com.project.doctorinsta.doctor_ui.bookings;

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
import com.project.doctorinsta.adapter.PatientBookingsAdapter;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.PatientBooking;
import com.project.doctorinsta.data.PatientBookingInfo;
import com.project.doctorinsta.data.Specialisation;
import com.project.doctorinsta.patient_ui.booking.MyBookingsFragment;
import com.project.doctorinsta.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class BookingFragment extends Fragment {

    private PatientBookingsAdapter bookingsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private SharedPrefs sharedPrefs;
    private MaterialDialog materialDialog;

    private static final String ARG_PARAM1 = "IS_TODAY_BOOKINGS";
    private static final String ARG_PARAM2 = "IS_UPCOMING_BOOKINGS";
    private static final String ARG_PARAM3 = "IS_PAST_BOOKINGS";

    public BookingFragment() {
        // Required empty public constructor
    }

    private  static boolean IS_TODAY_BOOKINGS;
    private  static boolean IS_UPCOMING_BOOKINGS;
    private  static boolean IS_PAST_BOOKINGS;

    public static MyBookingsFragment newInstance(boolean param1, boolean param2, boolean param3) {
        MyBookingsFragment fragment = new MyBookingsFragment();
        Bundle args = new Bundle();
        IS_TODAY_BOOKINGS =param1;
        IS_UPCOMING_BOOKINGS =param2;
        IS_PAST_BOOKINGS =param2;
        args.putBoolean(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, param2);
        args.putBoolean(ARG_PARAM3, param3);
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
        Doctor doctor = sharedPrefs.getDoctor("loggedInDoctor");
        materialDialog = new MaterialDialog.Builder(getActivity())
                .title("Loading your bookings")
                .content("Please wait ...")
                .progress(true, 0).show();
        DatabaseReference dbSchedulesRef = FirebaseDatabase.getInstance().getReference("schedules");
        Query query = dbSchedulesRef.orderByChild("doctorIdNumber").equalTo(doctor.getIdNumber());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                List<Long> scheduleIds = new ArrayList<>();
                List<PatientBookingInfo> bookings = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Long idFromDb = snapshot.child("id").getValue(Long.class);
                    scheduleIds.add(idFromDb);
                }
                DatabaseReference dbSchedulesRef = FirebaseDatabase.getInstance().getReference("schedules");
                Query scheList = dbSchedulesRef.orderByChild("id");
                scheList.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        List<PatientBooking> patientBookings = new ArrayList<>();
                        for (Long scheduleId : scheduleIds) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Long idFromDb = snapshot.child("id").getValue(Long.class);
                                if (idFromDb != null && idFromDb.equals(scheduleId)) {
                                    Log.d("found user schedule", " {} " + snapshot.toString());
                                    String date = snapshot.child("date").getValue(String.class);
                                    String from = snapshot.child("startTime").getValue(String.class);
                                    String to = snapshot.child("endTime").getValue(String.class);
                                    String status = snapshot.child("status").getValue(String.class);
                                    String patientPhone = snapshot.child("patientPhone").getValue(String.class);
                                    PatientBooking patientBooking = new PatientBooking( patientPhone,
                                            idFromDb,  date,  from, to,  status);
                                    patientBookings.add(patientBooking);
                                }
                            }
                        }
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("patients");
                        Query docList = dbRef.orderByChild("phone");
                        docList.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                for (PatientBooking patientBooking : patientBookings) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String phoneNumberFromDb = snapshot.child("phone").getValue(String.class);
                                        if (phoneNumberFromDb != null && phoneNumberFromDb.equals(patientBooking.getPatientPhone())) {
                                            Log.d("found patient", ":" + snapshot.toString());
                                            String firstname = snapshot.child("firstname").getValue(String.class);
                                            String lastname = snapshot.child("lastname").getValue(String.class);
                                            final PatientBookingInfo booking = new PatientBookingInfo( firstname + "" + lastname,
                                                   phoneNumberFromDb,""+ patientBooking.getDate(),
                                                    ""+patientBooking.getStartTime(), patientBooking.getEndTime());
                                            int yr= Calendar.getInstance().get(Calendar.YEAR);
                                            int bkYr= Integer.parseInt(patientBooking.getDate().substring(6));
                                            int month= Calendar.getInstance().get(Calendar.MONTH)+1;
                                            int bkMonth= Integer.parseInt(patientBooking.getDate().substring(3,5));
                                            int day= Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                                            int bkDay= Integer.parseInt(patientBooking.getDate().substring(0,2));
                                            IS_UPCOMING_BOOKINGS= true;
                                            if(IS_TODAY_BOOKINGS ){
                                                if(bkYr==yr && bkMonth==month && day==bkDay){
                                                    bookings.add(booking);
                                                }
                                            }
                                            if(IS_UPCOMING_BOOKINGS ){
                                                if(bkYr>=yr && bkMonth>=month && bkDay>day){
                                                    bookings.add(booking);
                                                }
                                            }
                                            if(IS_PAST_BOOKINGS ){
                                                if(bkYr<=yr && bkMonth<=month && bkDay<day){
                                                    bookings.add(booking);
                                                }
                                            }

                                        }
                                    }
                                }
                                if (materialDialog.isShowing()) {
                                    materialDialog.dismiss();
                                }
                                linearLayoutManager = new LinearLayoutManager(getActivity(),
                                        LinearLayoutManager.VERTICAL, false);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                bookingsAdapter = new PatientBookingsAdapter(getActivity(), bookings);
                                recyclerView.setAdapter(bookingsAdapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Getting Post failed, log a message
                                Log.w(TAG, "load patients:onCancelled", databaseError.toException());
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
