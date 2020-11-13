package com.project.doctorinsta.ui.booking;


import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.android.material.card.MaterialCardView;
import com.project.doctorinsta.PatientDashboardActivity;
import com.project.doctorinsta.R;
import com.project.doctorinsta.SharedPrefs;
import com.project.doctorinsta.data.Booking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        getSupportActionBar().setTitle("Bookings");
        Intent intent = getIntent();
        String fragmentName = intent.getStringExtra("fragmentName");
        if (fragmentName != null) {
            switch (fragmentName) {
                case "Make Booking":
                    getSupportFragmentManager().beginTransaction().replace(R.id.bookingsFragmentContainer,
                            MakeBookingFragment.newInstance("", "")).commit();
                    getSupportActionBar().setTitle("Make Booking");
                    break;
                default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.bookingsFragmentContainer,
                            MakeBookingFragment.newInstance("", "")).commit();
                    getSupportActionBar().setTitle("Make Booking");
                    break;
            }
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.bookingsFragmentContainer,
                    MakeBookingFragment.newInstance("", "")).commit();
            getSupportActionBar().setTitle("Make Booking");
        }
    }
}