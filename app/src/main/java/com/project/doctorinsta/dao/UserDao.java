package com.project.doctorinsta.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.doctorinsta.data.User;

@Dao
public interface UserDao {

    @Insert
    void insertAll(User... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long save(User user);

    @Query("select * from `User` where id=:id")
    User findById(Long id);

    @Query("select * from `User` where idNumber=:idNumber")
    User findByIdNumber(int idNumber);

    @Query("select * from `User` where email=:email and password=:password")
    User findByEmailAndPass(String email, String password);

    @Query("select * from `User` where email=:email")
    User findByEmail(String email);

    @Update
    void update(User user);


    @Delete
    void delete(User user);
}
