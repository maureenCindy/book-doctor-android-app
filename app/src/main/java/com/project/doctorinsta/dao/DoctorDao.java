package com.project.doctorinsta.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.doctorinsta.data.Doctor;

import java.util.List;

@Dao
public interface DoctorDao {

    @Insert
    void insertAll(Doctor... doctors);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long save(Doctor doctor);

    @Query("select * from `Doctor` limit 10")
   List<Doctor> findAll();

    @Query("select * from `Doctor` where idNumber=:idNumber")
    Doctor findByIdNumber(int idNumber);

    @Query("select * from `Doctor` where specialisationIdNumber=:specialisationId")
    LiveData<List<Doctor>> findAllBySpecialisationId(String specialisationId);

    @Query("select * from `Doctor` where id=:id")
    Doctor findById(Long id);

    @Update
    void update(Doctor doctor);


    @Delete
    void delete(Doctor doctor);
}
