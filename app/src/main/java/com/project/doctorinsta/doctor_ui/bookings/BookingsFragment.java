package com.project.doctorinsta.doctor_ui.bookings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.project.doctorinsta.R;
import com.project.doctorinsta.adapter.BookingsPagerAdapter;

public class BookingsFragment extends Fragment {

    private ViewPager viewPager;
    TabLayout tabLayout;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookings, container, false);
        viewPager =root.findViewById(R.id.vpPager);
        tabLayout =root.findViewById(R.id.tab_layout);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        BookingsPagerAdapter bookingsPagerAdapter = new BookingsPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(bookingsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
