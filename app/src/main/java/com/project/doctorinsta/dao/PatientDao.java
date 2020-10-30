package com.project.doctorinsta.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.doctorinsta.data.Patient;

@Dao
public interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long save(Patient patient);

    @Query("select * from `Patient` where id=:id")
    Patient findById(Long id);

    @Query("select * from `Patient` where userId=:userId")
    Patient findByUserId(Long userId);

    @Update
    void update(Patient patient);


    @Delete
    void delete(Patient patient);
}
