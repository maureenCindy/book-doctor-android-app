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
import com.project.doctorinsta.data.Booking;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.PatientBooking;
import com.project.doctorinsta.data.PatientBookingInfo;
import com.project.doctorinsta.data.Specialisation;
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


    public BookingFragment() {
        // Required empty public constructor
    }

    private  static boolean IS_TODAY_BOOKINGS;
    private  static boolean IS_UPCOMING_BOOKINGS;
    private  static boolean IS_PAST_BOOKINGS;

    public static BookingFragment newInstance(boolean param1, boolean param2, boolean param3) {
        BookingFragment fragment = new BookingFragment();
        IS_TODAY_BOOKINGS =param1;
        IS_UPCOMING_BOOKINGS =param2;
        IS_PAST_BOOKINGS =param3;
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
        if(doctor!=null){
            materialDialog = new MaterialDialog.Builder(getActivity())
                    .title("Loading your bookings")
                    .content("Please wait ...")
                    .progress(true, 0).show();
            DatabaseReference dbSchedulesRef = FirebaseDatabase.getInstance().getReference("bookings");
            Query query = dbSchedulesRef.orderByChild("doctorIdNumber").equalTo(doctor.getIdNumber());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                    List<Booking> bookingSchedules = new ArrayList<>();
                    List<PatientBookingInfo> bookings = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Long scheduleIdFromDb = snapshot.child("scheduleId").getValue(Long.class);
                        String patientPhone = snapshot.child("patientPhone").getValue(String.class);
                        Booking booking = new Booking();
                        booking.setPatientPhone(patientPhone);
                        booking.setScheduleId(scheduleIdFromDb);
                        bookingSchedules.add(booking);

                    }
                    DatabaseReference dbSchedulesRef = FirebaseDatabase.getInstance().getReference("schedules");
                    Query scheList = dbSchedulesRef.orderByChild("id");
                    scheList.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            List<PatientBooking> patientBookings = new ArrayList<>();
                            for (Booking booking : bookingSchedules) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Long idFromDb = snapshot.child("id").getValue(Long.class);
                                    String statusFromDb = snapshot.child("status").getValue(String.class);
                                    if (statusFromDb!=null && statusFromDb.equalsIgnoreCase("closed")
                                            && idFromDb != null && idFromDb.equals(booking.getScheduleId())) {
                                        Log.d("found schedule", " {} " + snapshot.toString());
                                        String date = snapshot.child("date").getValue(String.class);
                                        String from = snapshot.child("startTime").getValue(String.class);
                                        String to = snapshot.child("endTime").getValue(String.class);
                                        String status = snapshot.child("status").getValue(String.class);
                                        PatientBooking patientBooking = new PatientBooking( booking.getPatientPhone(),
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
                                                final PatientBookingInfo booking = new PatientBookingInfo( firstname + " " + lastname,
                                                        phoneNumberFromDb,""+ patientBooking.getDate(),
                                                        ""+patientBooking.getStartTime(), patientBooking.getEndTime());
                                                int yr= Calendar.getInstance().get(Calendar.YEAR);
                                                int bkYr= Integer.parseInt(patientBooking.getDate().substring(6));
                                                int month= Calendar.getInstance().get(Calendar.MONTH)+1;
                                                int bkMonth= Integer.parseInt(patientBooking.getDate().substring(3,5));
                                                int day= Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                                                int bkDay= Integer.parseInt(patientBooking.getDate().substring(0,patientBooking.getDate().indexOf("-")));
                                                //calendar
                                                Log.d("bkYr", ": "+ bkYr);
                                                Log.d("calendar yr", ": "+ yr);
                                                Log.d("bkMonth", ": "+ bkMonth);
                                                Log.d("calendar month", ": "+ month);
                                                Log.d("bkDay", ": "+ bkDay);
                                                Log.d("calendar day", ": "+ day);
                                                if(IS_TODAY_BOOKINGS ){
                                                    Log.d("*** IS_TODAY_BOOKINGS ", "****");
                                                    if(bkYr==yr && bkMonth==month && day==bkDay){
                                                        bookings.add(booking);
                                                    }
                                                }else if(IS_UPCOMING_BOOKINGS ){
                                                    Log.d("*** IS_UPCOMING_", "BOOKINGS ****");
                                                    if((bkYr==yr && bkMonth==month && bkDay>day) ||
                                                            ( bkYr==yr && bkMonth>month) ||
                                                            (bkYr>yr)){
                                                        bookings.add(booking);
                                                    }
                                                }else if(IS_PAST_BOOKINGS ){
                                                    Log.d("*** IS_PAST_BOOKINGS ", "****");
                                                    if((bkYr==yr && bkMonth==month && bkDay<day) ||
                                                            ( bkYr==yr && bkMonth<month) ||
                                                            (bkYr<yr)){
                                                        bookings.add(booking);
                                                    }
                                                }

                                            }
                                        }
                                    }
                                    if (materialDialog.isShowing()) {
                                        materialDialog.dismiss();
                                    }
                                    Log.d("bookings found has size", ": "+ bookings.size());
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
