package com.project.doctorinsta.ui.doctors;

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

import com.project.doctorinsta.R;
import com.project.doctorinsta.adapter.DoctorAdapter;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.ui.specialisation.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class DoctorsFragment extends Fragment {

    private DoctorAdapter doctorAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_doctors,container,false);
        recyclerView = root.findViewById(R.id.recyclerView);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<Doctor> doctors = new ArrayList<>();
        Log.d("spec from db"," :"+doctors.toString());
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        doctorAdapter = new DoctorAdapter(getActivity(), doctors);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 5, true));
        recyclerView.setAdapter(doctorAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
