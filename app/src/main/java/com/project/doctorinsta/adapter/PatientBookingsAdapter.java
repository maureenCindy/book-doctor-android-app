package com.project.doctorinsta.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.doctorinsta.R;
import com.project.doctorinsta.data.PatientBookingInfo;

import java.util.List;

public class PatientBookingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<PatientBookingInfo> bookings;
    private final Context context;
    private LayoutInflater layoutInflater;

    public PatientBookingsAdapter(Context context, List<PatientBookingInfo> items) {
        this.bookings = items;
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);

    }

    @Override
    public PatientBookingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = layoutInflater.inflate(R.layout.booking_item, parent, false);
        return new PatientBookingViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        PatientBookingViewHolder bookingViewHolder = (PatientBookingViewHolder)viewHolder;
        final PatientBookingInfo booking = bookings.get(position);
        Log.d("booking found="," :"+booking.toString());
        bookingViewHolder.patientName.setText(booking.getPatient());
        bookingViewHolder.phone.setText(booking.getPhone());
        bookingViewHolder.date.setText(booking.getDate());
        bookingViewHolder.timeslot.setText(booking.getStart()+"-"+booking.getEnd());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class PatientBookingViewHolder extends RecyclerView.ViewHolder {
        TextView patientName;
        TextView phone;
        TextView date;
        TextView timeslot;
        public PatientBookingViewHolder(View view) {
            super(view);
            patientName= view.findViewById(R.id.tvDocBooked);
            phone= view.findViewById(R.id.tvDocSpecialty);
            date= view.findViewById(R.id.tvBookDate);
            timeslot= view.findViewById(R.id.tvBookedTimeSlot);
            TextView address= view.findViewById(R.id.tvDocAddress);
            address.setVisibility(View.GONE);
        }
    }

}
