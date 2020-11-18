package com.project.doctorinsta.doctor_ui.schedules;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.michaldrabik.classicmaterialtimepicker.CmtpDialogFragment;
import com.project.doctorinsta.R;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.Schedule;
import com.project.doctorinsta.doctor_ui.dashboard.DoctorDashboardActivity;
import com.project.doctorinsta.utils.SharedPrefs;
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import java.util.Calendar;

public class AddScheduleActivity extends AppCompatActivity implements View.OnClickListener, OnDateSelectedListener {
    SharedPrefs sharedPrefs;
    MaterialDialog materialDialog;
    TextView date, from, to, addNewSchedule;
    CmtpDialogFragment startTimePickerFrag;
    CmtpDialogFragment endTimePickerFrag;
    DatePickerTimeline datePickerTimeline;
    String dateSelected;
    String timeStart;
    String timeEnd;
    Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs =SharedPrefs.getInstance(this);
        doctor = sharedPrefs.getDoctor("loggedInDoctor");
        getSupportActionBar().setTitle("Add New Schedule");
        setContentView(R.layout.activity_addschedule);
        datePickerTimeline = findViewById(R.id.datePickerTimeline);
        datePickerTimeline.setInitialDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE));
        datePickerTimeline.setOnDateSelectedListener(this);
        date = findViewById(R.id.tvDate);
        from = findViewById(R.id.tvStartTime);
        to = findViewById(R.id.tvEndTime);
        addNewSchedule=findViewById(R.id.tvAddNewSchedule);
        from.setOnClickListener(this);
        to.setOnClickListener(this);
        addNewSchedule.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvStartTime:
                startTimePickerFrag = CmtpDialogFragment.newInstance();
                startTimePickerFrag.setInitialTime24(23,30);
                startTimePickerFrag.setOnTime24PickedListener(time24 -> {
                    from.setTextColor(Color.GREEN);
                    from.setText("START TIME: "+time24.getHour()+":"+time24.getMinute());
                    timeStart = time24.getHour()+":"+time24.getMinute();
                });
                startTimePickerFrag.show(getSupportFragmentManager(), "Choose start-time*");
                break;
            case R.id.tvEndTime:
                endTimePickerFrag = CmtpDialogFragment.newInstance();
                endTimePickerFrag.setInitialTime24(23, 30);
                endTimePickerFrag.setOnTime24PickedListener(cmtpTime24 -> {
                    to.setTextColor(Color.GREEN);
                    to.setText("END-TIME: "+cmtpTime24.getHour()+":"+cmtpTime24.getMinute());
                    timeEnd = cmtpTime24.getHour()+":"+cmtpTime24.getMinute();
                });
                endTimePickerFrag.show(getSupportFragmentManager(), "Choose end-time*");
                break;
            case R.id.tvAddNewSchedule:
                if(isEmpty(dateSelected) ){
                 showError("Date is required!", "Invalid schedule", view.getContext());
                }else if(isEmpty(timeStart)){
                    showError("Start time is required!", "Invalid schedule", view.getContext());
                }else if(isEmpty(timeEnd)){
                    showError("End time is required!", "Invalid schedule", view.getContext());
                }else if (isInvalidTimeSlot(timeStart, timeEnd)){
                    showError("Invalid time slot!", "Invalid schedule", view.getContext());
                }else{
                    materialDialog = new MaterialDialog.Builder(view.getContext())
                            .title("New Schedule Confirmation")
                            .cancelable(true)
                            .content("Date:"+dateSelected+", between "+timeStart+"-"+timeEnd)
                            .positiveText("Confirm")
                            .negativeText("Cancel")
                            .onPositive((dialog, which) -> {
                                //   saving data
                                if(doctor!=null && doctor.getIdNumber()!=null){
                                    Long generatedID =Calendar.getInstance().getTimeInMillis();
                                    DatabaseReference dbUserRef = FirebaseDatabase.getInstance().getReference("schedules");
                                    Schedule schedule = new Schedule(generatedID, doctor.getIdNumber(), dateSelected, timeStart, timeEnd, "open");
                                    dbUserRef.child(String.valueOf(generatedID)).setValue(schedule);
                                    if (materialDialog.isShowing()) {
                                        materialDialog.dismiss();
                                    }
                                    Toast.makeText(this, "New schedule has been added successfully.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AddScheduleActivity.this, DoctorDashboardActivity.class));
                                    finish();
                                }
                            })
                            .onNegative((dialog, which) -> {
                                Toast.makeText(view.getContext(),"New Scheduling cancelled", Toast.LENGTH_LONG).show();
                            })
                            .show();
                }
                break;
        }
    }

    @Override
    public void onDateSelected(int year, int month, int day, int dayOfWeek) {
        month=month+1;
        dateSelected = day+"-"+month+"-"+year;
    }

    @Override
    public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {

    }

    private boolean isEmpty(String val){
        return val ==null || val.isEmpty();
    }

    private void showError(String error, String title, Context context){
        materialDialog = new MaterialDialog.Builder(context)
                .title(title)
                .cancelable(true)
                .content(error)
                .positiveText("OK")
                .negativeText("Cancel")
                .show();
    }

    private boolean isInvalidTimeSlot(String start, String end){
       int startHour = Integer.parseInt(start.substring(0,start.indexOf(":")));
       int endHour = Integer.parseInt(end.substring(0,start.indexOf(":")));
       return endHour<startHour;
    }
}