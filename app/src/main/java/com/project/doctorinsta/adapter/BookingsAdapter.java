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
import com.project.doctorinsta.data.BookingInfo;

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<BookingInfo> bookings;
    private final Context context;
    private LayoutInflater layoutInflater;

    public BookingsAdapter(Context context, List<BookingInfo> items) {
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
        final BookingInfo booking = bookings.get(position);
        Log.d("booking found="," :"+booking.toString());
        bookingViewHolder.doctorName.setText(booking.getDoctor());
        bookingViewHolder.doctorProf.setText(booking.getSpecialty());
        bookingViewHolder.date.setText(booking.getDate());
        bookingViewHolder.timeSlot.setText(booking.getStart()+"-"+booking.getEnd());
        bookingViewHolder.address.setText(booking.getAddress());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName;
        TextView doctorProf;
        TextView date;
        TextView timeSlot;
        TextView address;
        public BookingViewHolder(View view) {
            super(view);
            doctorName= view.findViewById(R.id.tvDocBooked);
            doctorProf= view.findViewById(R.id.tvDocSpecialty);
            date= view.findViewById(R.id.tvBookDate);
            timeSlot= view.findViewById(R.id.tvBookedTimeSlot);
            address= view.findViewById(R.id.tvDocAddress);
        }
    }

}
