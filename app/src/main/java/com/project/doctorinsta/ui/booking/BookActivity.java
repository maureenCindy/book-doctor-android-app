package com.project.doctorinsta.ui.booking;


import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;
import com.project.doctorinsta.PatientDashboardActivity;
import com.project.doctorinsta.R;
import com.project.doctorinsta.SharedPrefs;
import com.project.doctorinsta.dao.BookingDao;
import com.project.doctorinsta.dao.DoctorDao;
import com.project.doctorinsta.dao.DoctorScheduleDao;
import com.project.doctorinsta.dao.PatientDao;
import com.project.doctorinsta.dao.UserDao;
import com.project.doctorinsta.data.Booking;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.DoctorSchedule;
import com.project.doctorinsta.data.Patient;
import com.project.doctorinsta.data.User;
import com.project.doctorinsta.database.DoctorInstaDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookActivity extends AppCompatActivity {

    MaterialDialog materialDialog;
    SharedPrefs sharedPrefs;
    TimetableView timetableView;
    WeekView weekView;
    static final int TYPE_DAY_VIEW = 1;
    static final int TYPE_THREE_DAY_VIEW = 2;
    static final int TYPE_WEEK_VIEW = 3;
    int mWeekViewType = TYPE_THREE_DAY_VIEW;
    Long doctorId;
    Long userId;
    ArrayList<Schedule> schedules = new ArrayList<>();
    List<DoctorSchedule> doctorSchedules = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        getSupportActionBar().setTitle("Book Now");
        sharedPrefs = SharedPrefs.getInstance(this);
        DoctorDao doctorDao = DoctorInstaDatabase.getDatabase(this).doctorDao();
        doctorId = sharedPrefs.getLongValue("doctorId");
        userId = sharedPrefs.getLongValue("userId");
        Doctor doctor = doctorDao.findById(doctorId);
        DoctorScheduleDao doctorScheduleDao = DoctorInstaDatabase.getDatabase(this).doctorScheduleDao();
        doctorSchedules = doctorScheduleDao.findAllByDoctorIdNumber(doctor.getIdNumber());
        PatientDao patientDao = DoctorInstaDatabase.getDatabase(this).patientDao();
        BookingDao bookingDao = DoctorInstaDatabase.getDatabase(this).bookingDao();
        weekView = (WeekView) findViewById(R.id.weekView);
        WeekView.EventClickListener mEventClickListener = new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                Long patientId;
                if (patientDao.findByUserId(userId) == null) {
                    Patient patient = new Patient();
                    patient.setUserId(userId);
                    patient.setGender("Male");
                    patientId = patientDao.save(patient);
                } else {
                    patientId = patientDao.findByUserId(userId).getId();
                }
                sharedPrefs.setLongValue("patientID",patientId);
                Booking booking = new Booking();
                booking.setDate(new Date());
                booking.setDoctorId(doctorId);
                booking.setPatientId(patientId);
                booking.setStartTime(event.getStartTime().get(Calendar.HOUR_OF_DAY));
                booking.setEndTime(event.getEndTime().get(Calendar.HOUR_OF_DAY));
                List<String> days = Arrays.asList(getResources().getStringArray(R.array.daysOfWeek));
                booking.setDay(days.get(event.getStartTime().get(Calendar.DAY_OF_WEEK)));
                bookingDao.save(booking);
                for (DoctorSchedule doctorSchedule : doctorSchedules) {
                    if (doctorSchedule.getStartTime() == event.getStartTime().get(Calendar.HOUR_OF_DAY)
                            && doctorSchedule.getEndTime() == event.getEndTime().get(Calendar.HOUR_OF_DAY)) {
                        doctorScheduleDao.delete(doctorSchedule);
                    }
                }
                Intent intent = new Intent(getBaseContext(), PatientDashboardActivity.class);
                intent.putExtra("fragmentName","MyBookings");
                startActivity(intent);
                finish();
            }
        };
        weekView.setOnEventClickListener(mEventClickListener);
        MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.
                List<WeekViewEvent> events = getEvents(newYear, newMonth);
                return events;
            }
        };

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        weekView.setMonthChangeListener(mMonthChangeListener);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);
    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        weekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());
                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    private List<WeekViewEvent> getEvents(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        for (int i = 0; i < doctorSchedules.size(); i++) {
            DoctorSchedule doctorSchedule = doctorSchedules.get(i);
            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, doctorSchedule.getStartTime());
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.MONTH, newMonth - 1);
            startTime.set(Calendar.YEAR, newYear);
            Calendar endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.HOUR, doctorSchedule.getEndTime() - doctorSchedule.getStartTime());
            endTime.set(Calendar.MONTH, newMonth - 1);
            WeekViewEvent event = new WeekViewEvent(i, getEventTitle(startTime, endTime), startTime, endTime);
            event.setColor(getResources().getColor(R.color.purple_700));
            events.add(event);
        }
        return events;
    }

    private String getEventTitle(Calendar startTime, Calendar endTime) {
        return startTime.get(Calendar.HOUR) + ":" + endTime.get(Calendar.HOUR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id) {
            case R.id.action_today:
                weekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    weekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    weekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    weekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    weekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    weekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    weekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    weekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    weekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    weekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    weekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    weekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    weekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}