package com.project.doctorinsta.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.doctorinsta.R;
import com.project.doctorinsta.SharedPrefs;
import com.project.doctorinsta.dao.SpecialisationDao;
import com.project.doctorinsta.dao.UserDao;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.Specialisation;
import com.project.doctorinsta.data.User;
import com.project.doctorinsta.database.DoctorInstaDatabase;
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
        sharedPrefs =SharedPrefs.getInstance(context);
        sharedPrefs.setLongValue("doctorId",doctor.getId());
        UserDao userDao = DoctorInstaDatabase.getDatabase(context).userDao();
        SpecialisationDao specialisationDao = DoctorInstaDatabase.getDatabase(context).specialisationDao();
        User user =userDao.findByIdNumber(doctor.getIdNumber());
        Specialisation specialisation = specialisationDao.findByIdNumber(doctor.getSpecialisationIdNumber());
        doctorViewHolder.name.setText(user.getFirstname() + " "+ user.getLastname());
        doctorViewHolder.speciality.setText(specialisation.getName());
        doctorViewHolder.rate.setText(doctor.getRate());
        doctorViewHolder.bookLink.setOnClickListener(v->{
            Intent intent = new Intent(context, BookActivity.class);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView speciality;
        TextView rate;
        TextView bookLink;
        public DoctorViewHolder(View view) {
            super(view);
            name= view.findViewById(R.id.tvDoctorName);
            speciality= view.findViewById(R.id.tvDoctorSpeciality);
            rate= view.findViewById(R.id.tvDoctorRate);
            bookLink= view.findViewById(R.id.tvBookNowLink);
        }
    }
}
