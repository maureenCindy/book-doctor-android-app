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
import com.project.doctorinsta.data.Booking;

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Booking> bookings;
    private final Context context;
    private LayoutInflater layoutInflater;

    public BookingsAdapter(Context context, List<Booking> items) {
        this.bookings = items;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public BookingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = layoutInflater.inflate(R.layout.booking_item, parent, false);
        return new BookingViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        BookingViewHolder bookingViewHolder = (BookingViewHolder)viewHolder;
        final Booking booking = bookings.get(position);
        Log.d("booking found="," :"+booking.toString());
        bookingViewHolder.date.setText("Date: dummy ");
        bookingViewHolder.timeSlot.setText("Time:09"+ "00-"+ "1200 HRS");
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName;
        TextView date;
        TextView timeSlot;
        public BookingViewHolder(View view) {
            super(view);
            doctorName= view.findViewById(R.id.tvDocBooked);
            date= view.findViewById(R.id.tvBookDate);
            timeSlot= view.findViewById(R.id.tvBookTimeSlot);
        }
    }

}
