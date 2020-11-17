package com.project.doctorinsta.patient_ui.booking;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.project.doctorinsta.R;

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
                case "My Bookings":
                    getSupportFragmentManager().beginTransaction().replace(R.id.bookingsFragmentContainer,
                            MyBookingsFragment.newInstance("", "")).commit();
                    getSupportActionBar().setTitle("My Bookings");
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