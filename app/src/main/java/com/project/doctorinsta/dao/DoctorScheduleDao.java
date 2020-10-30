package com.project.doctorinsta.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.doctorinsta.data.DoctorSchedule;

import java.util.List;

@Dao
public interface DoctorScheduleDao {

    @Insert
    void insertAll(DoctorSchedule... doctorSchedules);

    @Query("select * from `DoctorSchedule` where idNumber=:idNumber")
    DoctorSchedule findByIdNumber(int idNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long save(DoctorSchedule doctorSchedule);

    @Query("select * from `DoctorSchedule` where doctorId=:doctorId")
    List<DoctorSchedule> findAllByDoctorId(Long doctorId);

    @Update
    void update(DoctorSchedule doctorSchedule);


    @Delete
    void delete(DoctorSchedule doctorSchedule);


}
