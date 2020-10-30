package com.project.doctorinsta.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.doctorinsta.data.Booking;

import java.util.List;

@Dao
public interface BookingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long save(Booking booking);


    @Query("select * from `Booking` where patientId=:patientId")
    List<Booking> findAllByPatientId(Long patientId);

    @Query("select * from `Booking` where doctorId=:doctorId")
    LiveData<List<Booking>> findAllByDoctorId(Long doctorId);

    @Update
    void update(Booking booking);

    @Delete
    void delete(Booking booking);
}
