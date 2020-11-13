package com.project.doctorinsta.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.doctorinsta.R;
import com.project.doctorinsta.data.Booking;
import com.project.doctorinsta.data.Schedule;
import com.project.doctorinsta.ui.booking.BookActivity;

import java.util.List;

public class SlotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Schedule> slots;
    private final Context context;
    private LayoutInflater layoutInflater;

    public SlotAdapter(Context context, List<Schedule> items) {
        this.slots = items;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public SlotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = layoutInflater.inflate(R.layout.slot_item, parent, false);
        return new SlotViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        SlotViewHolder slotViewHolder = (SlotViewHolder)viewHolder;
        final Schedule slot = slots.get(position);
        Log.d("slot found, ","with start time="+slot.getStartTime());
        slotViewHolder.start.setText(slot.getStartTime());
        slotViewHolder.end.setText(slot.getEndTime());
        slotViewHolder.itemView.setOnClickListener(v->{
            //todo
            /*Intent intent = new Intent(context, ConfirmBooking.class);
            v.getContext().startActivity(intent);*/
            Toast.makeText(context,"Confirm booking", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    public static class SlotViewHolder extends RecyclerView.ViewHolder {
        TextView start;
        TextView end;
        public SlotViewHolder(View view) {
            super(view);
            start= view.findViewById(R.id.time_from);
            end= view.findViewById(R.id.time_to);
        }
    }

}
