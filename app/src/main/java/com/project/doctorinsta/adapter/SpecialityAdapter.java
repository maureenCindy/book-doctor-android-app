package com.project.doctorinsta.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.project.doctorinsta.R;
import com.project.doctorinsta.data.Specialisation;
import com.project.doctorinsta.patient_ui.dashboard.PatientDashboardActivity;
import com.project.doctorinsta.utils.SharedPrefs;

import java.util.List;

public class SpecialityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Specialisation> specialisations;
    private final Context context;
    private LayoutInflater layoutInflater;

    public SpecialityAdapter(Context context, List<Specialisation> items) {
        this.specialisations = items;
        this.context = context;
    }

    @Override
    public SpecialisationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = layoutInflater.inflate(R.layout.specialisation_item, parent, false);
        return new SpecialisationViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        SpecialisationViewHolder specialisationViewHolder = (SpecialisationViewHolder)viewHolder;
        final Specialisation specialisation = specialisations.get(position);
        specialisationViewHolder.name.setText(specialisation.getName());
        specialisationViewHolder.itemView.setOnClickListener(v -> {
            SharedPrefs sharedPrefs = SharedPrefs.getInstance(context);
            sharedPrefs.setLongValue("selectedSpecialityNumber",specialisation.getNumber());
            Intent intent = new Intent(v.getContext(), PatientDashboardActivity.class);
            intent.putExtra("fragmentName", "Doctors");
            v.getContext().startActivity(intent);
        });
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(40)
                .height(40)
                .endConfig()
                .buildRound(specialisations.get(position).getName().substring(0, 1), color1);
        specialisationViewHolder.imageView.setImageDrawable(drawable);
    }


    @Override
    public int getItemCount() {
        return specialisations.size();
    }

    public static class SpecialisationViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        public SpecialisationViewHolder(View view) {
            super(view);
            imageView= view.findViewById(R.id.image);
            name= view.findViewById(R.id.tvSpecialityName);
        }
    }
}
