package com.project.doctorinsta.ui.booking;


import android.os.Bundle;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;
import com.project.doctorinsta.R;
import com.project.doctorinsta.SharedPrefs;
import com.project.doctorinsta.dao.BookingDao;
import com.project.doctorinsta.dao.DoctorScheduleDao;
import com.project.doctorinsta.dao.PatientDao;
import com.project.doctorinsta.data.Booking;
import com.project.doctorinsta.data.DoctorSchedule;
import com.project.doctorinsta.data.Patient;
import com.project.doctorinsta.database.DoctorInstaDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BookActivity extends AppCompatActivity {

    MaterialDialog materialDialog;
    SharedPrefs sharedPrefs;
    TimetableView timetableView;
    Long doctorId;
    Long userId;
    ArrayList<Schedule> schedules = new ArrayList<>();
    List<DoctorSchedule> doctorSchedules = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        getSupportActionBar().setTitle("Book Now");
        sharedPrefs =SharedPrefs.getInstance(this);
        doctorId = sharedPrefs.getLongValue("doctorId");
        userId = sharedPrefs.getLongValue("userId");
        DoctorScheduleDao doctorScheduleDao = DoctorInstaDatabase.getDatabase(this).doctorScheduleDao();
        doctorSchedules = doctorScheduleDao.findAllByDoctorId(doctorId);
        for(DoctorSchedule doctorSchedule:doctorSchedules){
            Schedule schedule = new Schedule();
            schedule.setClassTitle("Open"); // sets subject
            schedule.setClassPlace("IT-601"); // sets place
            schedule.setProfessorName("Doc"); // sets professor
            schedule.setStartTime(new Time(doctorSchedule.getStartTime(),00)); // sets the beginning of class time (hour,minute)
            schedule.setEndTime(new Time(doctorSchedule.getEndTime(),00)); // sets the end of class time (hour,minute)
            schedules.add(schedule);
        }
        Toolbar toolbar= findViewById(R.id.toolbar);
        setContentView(R.layout.activity_booking);
        timetableView = findViewById(R.id.timetable);
        PatientDao patientDao = DoctorInstaDatabase.getDatabase(this).patientDao();
        BookingDao bookingDao = DoctorInstaDatabase.getDatabase(this).bookingDao();
        timetableView.setOnStickerSelectEventListener((idx, schedules) -> {
            Long patientId;
            if(patientDao.findByUserId(userId)==null){
                Patient patient = new Patient();
                patient.setUserId(userId);
                patient.setGender("Male");
                patientId = patientDao.save(patient);
            }else{
                patientId = patientDao.findByUserId(userId).getId();
            }
                Booking booking = new Booking();
                booking.setDate(new Date());
                booking.setDoctorId(doctorId);
                booking.setPatientId(patientId);
                booking.setStartTime(schedules.get(idx).getStartTime().getHour());
                booking.setEndTime(schedules.get(idx).getEndTime().getHour());
                List<String> days = Arrays.asList(getResources().getStringArray(R.array.default_header_title));
                booking.setDay(days.get(schedules.get(idx).getDay()));
                bookingDao.save(booking);
                for(DoctorSchedule doctorSchedule: doctorSchedules){
                    if(doctorSchedule.getStartTime()==schedules.get(idx).getStartTime().getHour()
                    && doctorSchedule.getEndTime()==schedules.get(idx).getEndTime().getHour() ){
                      doctorScheduleDao.delete(doctorSchedule);
                    }
                }
            });
        timetableView.add(schedules);
    }
}