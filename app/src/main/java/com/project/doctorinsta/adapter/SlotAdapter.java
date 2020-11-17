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

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.doctorinsta.R;
import com.project.doctorinsta.data.Booking;
import com.project.doctorinsta.data.Patient;
import com.project.doctorinsta.data.Schedule;
import com.project.doctorinsta.patient_ui.dashboard.PatientDashboardActivity;
import com.project.doctorinsta.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

public class SlotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Schedule> slots;
    private final Context context;
    private LayoutInflater layoutInflater;
    MaterialDialog materialDialog;

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
        SharedPrefs sharedPrefs = SharedPrefs.getInstance(context);
        if(sharedPrefs.getValue("userType").equalsIgnoreCase("patient")){
            slotViewHolder.itemView.setOnClickListener(v->{
                materialDialog = new MaterialDialog.Builder(context)
                        .title("Booking Confirmation")
                        .cancelable(true)
                        .content("On the "+slot.getDate()+" between "+slot.getStartTime()+"-"+slot.getEndTime())
                        .positiveText("Confirm")
                        .negativeText("Cancel")
                        .onPositive((dialog, which) -> {
                            //todo save booking
                            MaterialDialog internalMd =new MaterialDialog.Builder(context)
                                    .title("Saving booking")
                                    .content("Please wait ...")
                                    .progress(true, 0).show();
                            //   saving  new booking;
                            DatabaseReference dbBookingsRef = FirebaseDatabase.getInstance().getReference("bookings");
                            Patient patient = sharedPrefs.getPatient("loggedInPatient");
                            final Booking booking = new Booking(patient.getPhone(),slot.getId(),"open");
                            dbBookingsRef.child(String.valueOf(Calendar.getInstance().getTimeInMillis())).setValue(booking);
                            //update schedule to closed
                            DatabaseReference dbSchedulesRef = FirebaseDatabase.getInstance().getReference("schedules");
                            Query query = dbSchedulesRef.orderByChild("doctorIdNumber").equalTo(slot.getDoctorIdNumber());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        Log.d("exists",":true");
                                        Long id = snapshot.child(String.valueOf(slot.getDoctorIdNumber())).child("id").getValue(Long.class);
                                        if(id!=null && id.equals(slot.getId())){
                                            Log.d("found",":true");
                                            snapshot.child(String.valueOf(slot.getDoctorIdNumber())).child("status").getRef().setValue("closed");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                            if (internalMd.isShowing()) {
                                internalMd.dismiss();
                            }
                            Intent intent = new Intent(v.getContext(), PatientDashboardActivity.class);
                            intent.putExtra("fragmentName", "MyBookings");
                            v.getContext().startActivity(intent);
                        })
                        .onNegative((dialog, which) -> {
                            Toast.makeText(v.getContext(),"Booking cancelled", Toast.LENGTH_LONG).show();
                        })
                        .show();
            });
        }

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
