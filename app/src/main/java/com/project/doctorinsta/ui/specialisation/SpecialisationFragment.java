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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.doctorinsta.R;
import com.project.doctorinsta.adapter.SpecialityAdapter;
import com.project.doctorinsta.data.Specialisation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

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
         List<Specialisation> specialisations = new ArrayList<>();
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("specialisations");
        Query specList = dbRef.orderByChild("id");
        specList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                Log.d("found specialisations",":"+dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Log.d("found specialisation",":"+snapshot.toString());
                    String nameFromDb =snapshot.child("name").getValue(String.class);
                    String descFromDb =snapshot.child("description").getValue(String.class);
                    Specialisation specialisation = new Specialisation();
                    specialisation.setName(nameFromDb);
                    specialisation.setDescription(descFromDb);
                    specialisations.add(specialisation);
                }
                Log.d("found specialisations",":"+specialisations.size()+" for adapter");
                linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                specialityAdapter = new SpecialityAdapter(getActivity(), specialisations);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 2, true));
                recyclerView.setAdapter(specialityAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "load Specialisation:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
