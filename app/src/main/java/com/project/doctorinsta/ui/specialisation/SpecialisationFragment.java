package com.project.doctorinsta.ui.specialisation;

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
import com.project.doctorinsta.adapter.SpecialityAdapter;
import com.project.doctorinsta.dao.SpecialisationDao;
import com.project.doctorinsta.data.Specialisation;
import com.project.doctorinsta.database.DoctorInstaDatabase;

import java.util.List;

public class SpecialisationFragment extends Fragment {

    private SpecialityAdapter specialityAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_specialisation,container,false);
        recyclerView = root.findViewById(R.id.recyclerView);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SpecialisationDao dao = DoctorInstaDatabase.getDatabase(getActivity()).specialisationDao();
        List<Specialisation> specialisations = dao.findAll();
        Log.d("spec from db"," :"+specialisations.toString());
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        specialityAdapter = new SpecialityAdapter(getActivity(), specialisations);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 2, true));
        recyclerView.setAdapter(specialityAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
