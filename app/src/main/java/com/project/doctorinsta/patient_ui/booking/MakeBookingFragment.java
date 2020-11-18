package com.project.doctorinsta.patient_ui.booking;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.doctorinsta.R;
import com.project.doctorinsta.adapter.SlotAdapter;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.Schedule;
import com.project.doctorinsta.data.Specialisation;
import com.project.doctorinsta.patient_ui.specialisation.GridSpacingItemDecoration;
import com.project.doctorinsta.utils.SharedPrefs;
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MakeBookingFragment extends Fragment {

    private MaterialDialog materialDialog;
    private TextView docName, docSpeciality, docRate, docExperience, docFullAddress;
    private DatePickerTimeline datePickerTimeline;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerViewSlots;
    private SharedPrefs sharedPrefs;

    private Doctor doctor;
    private static final String DOCTORID = "param1";
    private static final String ARG_PARAM2 = "param2";

    public MakeBookingFragment() {
        // Required empty public constructor
    }

    private String mParam1;
    private String mParam2;

    public static MakeBookingFragment newInstance(String param1, String param2) {
        MakeBookingFragment fragment = new MakeBookingFragment();
        Bundle args = new Bundle();
        args.putString(DOCTORID, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_book_doctor, container, false);
        sharedPrefs =SharedPrefs.getInstance(getActivity());
        doctor = sharedPrefs.getDoctor("selectedDoctor");
        docName = root.findViewById(R.id.tvDocName);
        docName.setText("Dr "+doctor.getFirstname()+" "+doctor.getLastname());
        docSpeciality = root.findViewById(R.id.tvDocSpeciality);
        docSpeciality.setText(getSpecialityById(doctor.getSpecialtyIdNumber()));
        docRate = root.findViewById(R.id.tvDocRate);
        docRate.setText("Rate:"+doctor.getRate());
        docExperience = root.findViewById(R.id.tvDocExperience);
        docExperience.setText("Exp. "+doctor.getExperience()+ "years");
        docFullAddress = root.findViewById(R.id.tvDocFullAddress);
        docFullAddress.setText(doctor.getAddress()+","+doctor.getCity()+","+doctor.getCountry()+".");
        datePickerTimeline = root.findViewById(R.id.datePickerTimeline);
        datePickerTimeline.setDateTextColor(Color.WHITE);
        datePickerTimeline.setDayTextColor(Color.WHITE);
        datePickerTimeline.setMonthTextColor(Color.WHITE);
        datePickerTimeline.setInitialDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE));
        // Set a date Selected Listener
        datePickerTimeline.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int dayOfWeek) {
                Log.d("year=", String.valueOf(year));
                Log.d("month=", String.valueOf(month));
                Log.d("day=", String.valueOf(day));
                //todo getDoctorSchedules and load the recycler view
                materialDialog = new MaterialDialog.Builder(getActivity())
                        .title("fetching doctor's schedules")
                        .content("wait ...")
                        .progress(true, 0).show();
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("schedules");
                Query scheduleDbList = db.orderByChild("doctorIdNumber");
                scheduleDbList.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        List<Schedule> timeSlots = new ArrayList<>();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            Log.d("found schedule{}",childSnapshot.toString());
                            Long dbDoc = childSnapshot.child("doctorIdNumber").getValue(Long.class);
                            String availability = childSnapshot.child("status").getValue(String.class);
                            String dBdate = childSnapshot.child("date").getValue(String.class);
                            if(dbDoc!=null && dbDoc.equals(doctor.getIdNumber())&&
                                    availability!=null && !"".equalsIgnoreCase(availability) &&
                                    availability.equalsIgnoreCase("open") &&
                                    dBdate!=null && dBdate.length()==10 &&
                                    year==Integer.parseInt(dBdate.substring(6)) &&
                                    month+1==Integer.parseInt(dBdate.substring(3,5)) &&
                                    day==Integer.parseInt(dBdate.substring(0,dBdate.indexOf("-")))){
                                String startTime = childSnapshot.child("startTime").getValue(String.class);
                                String endTime = childSnapshot.child("endTime").getValue(String.class);
                                Long id = childSnapshot.child("id").getValue(Long.class);
                                Schedule schedule =new Schedule( id,doctor.getIdNumber(), dBdate, startTime, endTime, availability);
                                timeSlots.add(schedule);
                            }
                        }
                        if (materialDialog.isShowing()) {
                            materialDialog.dismiss();
                        }
                        recyclerViewSlots = root.findViewById(R.id.recyclerViewSlots);
                        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                        recyclerViewSlots.setLayoutManager(gridLayoutManager);
                        SlotAdapter mSlotAdapter = new SlotAdapter(getActivity(), timeSlots);
                        recyclerViewSlots.addItemDecoration(new GridSpacingItemDecoration(1, 2, true));
                        recyclerViewSlots.setAdapter(mSlotAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        if (materialDialog.isShowing()) {
                            materialDialog.dismiss();
                        }
                        new MaterialDialog.Builder(getActivity())
                                .title("Error")
                                .content(error.getMessage())
                                .positiveText("OK")
                                .show();
                    }
                });
            }

            @Override
            public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {
                // Do Something
            }
        });
        return root;
    }

    private String getSpecialityById(Long specialityIdNumber) {
        for(Specialisation sp:  sharedPrefs.getSpecialities("specialities")){
            if(sp.getNumber().equals(specialityIdNumber)){
                return sp.getName();
            }
        }
        return "invalid";
    }

    private List<Schedule> dummyData() {
        ArrayList<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule();
        schedule.setStartTime("09:00AM");
        schedule.setEndTime("09:45AM");
        Schedule schedule2 = new Schedule();
        schedule2.setStartTime("11:00AM");
        schedule2.setEndTime("12:45AM");
        schedules.add(schedule);
        Schedule schedule3 = new Schedule();
        schedule3.setStartTime("11:00PM");
        schedule3.setEndTime("12:45PM");
        Schedule schedule4 = new Schedule();
        schedule4.setStartTime("11:00PM");
        schedule4.setEndTime("12:45PM");
        schedules.add(schedule4);
        schedules.add(schedule3);
        schedules.add(schedule2);
        return schedules;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}