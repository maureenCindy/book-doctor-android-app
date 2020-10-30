package com.project.doctorinsta.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.doctorinsta.data.Specialisation;

import java.util.List;

@Dao
public interface SpecialisationDao {

    @Insert
    void insertAll(Specialisation... specialisations);

    @Query("select * from `Specialisation` where idNumber=:idNumber")
    Specialisation findByIdNumber(int idNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long save(Specialisation specialisation);

    @Query("select * from `Specialisation`")
    List<Specialisation> findAll();

    @Query("select * from `Specialisation` where id=:id")
    Specialisation findById(Long id);

    @Update
    void update(Specialisation specialisation);


    @Delete
    void delete(Specialisation specialisation);
}
