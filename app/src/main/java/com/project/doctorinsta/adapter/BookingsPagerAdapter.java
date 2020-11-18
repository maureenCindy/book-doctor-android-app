package com.project.doctorinsta.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.project.doctorinsta.doctor_ui.bookings.BookingFragment;

public class BookingsPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 3;

    public BookingsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BookingFragment.newInstance(true, false,false);
            case 1:
                return BookingFragment.newInstance(false,true, false);
            case 2:
                return BookingFragment.newInstance(false,false, true);
            default:
                return BookingFragment.newInstance(true, false,false);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0){
            return "Today";
        }
        if(position==1){
            return "Upcoming";
        }
        if(position==2){
            return "Past";
        }
        return "Today";
    }

}