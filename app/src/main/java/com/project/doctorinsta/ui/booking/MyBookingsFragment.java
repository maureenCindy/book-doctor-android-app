package com.project.doctorinsta.ui.booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.doctorinsta.R;
import com.project.doctorinsta.SharedPrefs;
import com.project.doctorinsta.adapter.BookingsAdapter;
import com.project.doctorinsta.data.Booking;

import java.util.ArrayList;
import java.util.List;


public class MyBookingsFragment extends Fragment {

    private BookingsAdapter bookingsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private SharedPrefs sharedPrefs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_patient_bookings,container,false);
        recyclerView = root.findViewById(R.id.recyclerView);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPrefs=SharedPrefs.getInstance(getActivity());
        Long patientID = sharedPrefs.getLongValue("patientID");
        List<Booking> bookings = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        bookingsAdapter = new BookingsAdapter(getActivity(), bookings);
        recyclerView.setAdapter(bookingsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
