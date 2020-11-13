package com.project.doctorinsta.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.doctorinsta.PatientDashboardActivity;
import com.project.doctorinsta.R;
import com.project.doctorinsta.SharedPrefs;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.Specialisation;
import com.project.doctorinsta.ui.booking.BookActivity;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Doctor> doctors;
    private final Context context;
    private LayoutInflater layoutInflater;
    private SharedPrefs sharedPrefs;

    public DoctorAdapter(Context context, List<Doctor> items) {
        this.doctors = items;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = layoutInflater.inflate(R.layout.doctor_item, parent, false);
        return new DoctorViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        DoctorViewHolder doctorViewHolder = (DoctorViewHolder)viewHolder;
        final Doctor doctor = doctors.get(position);
        doctorViewHolder.name.setText("Dr "+doctor.getFirstname() + " "+ doctor.getLastname());
        doctorViewHolder.speciality.setText(getDoctorSpeciality(doctor.getSpecialityIdNumber()));
        doctorViewHolder.rate.setText("Rate:"+doctor.getRate());
        doctorViewHolder.experience.setText("Exp:"+doctor.getExperience());
        doctorViewHolder.fullAddress.setText(doctor.getAddress()+","+doctor.getCity()+","+ doctor.getCountry()+".");
        doctorViewHolder.bookLink.setOnClickListener(v->{
            SharedPrefs sharedPrefs = SharedPrefs.getInstance(context);
            sharedPrefs.setDoctor("selectedDoctor", doctor);
            Intent intent = new Intent(v.getContext(), BookActivity.class);
            intent.putExtra("fragmentName", "Make Booking");
            v.getContext().startActivity(intent);

        });
    }

    private String getDoctorSpeciality(Long specialityIdNumber) {
        SharedPrefs sharedPrefs = SharedPrefs.getInstance(context);
        List<Specialisation> specialisations = sharedPrefs.getSpecialities("specialities");
        for(Specialisation sp : specialisations){
            if(sp.getNumber().equals(specialityIdNumber)){
                return sp.getName();
            }
        }
        return "invalid";
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView speciality;
        TextView rate;
        TextView experience;
        TextView fullAddress;
        TextView bookLink;
        public DoctorViewHolder(View view) {
            super(view);
            name= view.findViewById(R.id.tvDoctorName);
            speciality= view.findViewById(R.id.tvDoctorSpeciality);
            rate= view.findViewById(R.id.tvDoctorRate);
            experience= view.findViewById(R.id.tvDoctorExperience);
            fullAddress= view.findViewById(R.id.tvDoctorAddress);
            bookLink= view.findViewById(R.id.tvBookNowLink);
        }
    }
}
