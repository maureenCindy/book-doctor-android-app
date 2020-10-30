package com.project.doctorinsta.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.project.doctorinsta.dao.BookingDao;
import com.project.doctorinsta.dao.DoctorDao;
import com.project.doctorinsta.dao.DoctorScheduleDao;
import com.project.doctorinsta.dao.PatientDao;
import com.project.doctorinsta.dao.SpecialisationDao;
import com.project.doctorinsta.dao.UserDao;
import com.project.doctorinsta.data.Booking;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.DoctorSchedule;
import com.project.doctorinsta.data.Patient;
import com.project.doctorinsta.data.Specialisation;
import com.project.doctorinsta.data.User;


@Database(entities = {User.class, Specialisation.class, Patient.class, Doctor.class,
        DoctorSchedule.class, Booking.class}, version = 5)
@TypeConverters({Converters.class})
public abstract class DoctorInstaDatabase extends RoomDatabase {


    public abstract UserDao userDao();
    public abstract PatientDao patientDao();
    public abstract DoctorDao doctorDao();
    public abstract BookingDao bookingDao();
    public abstract SpecialisationDao specialisationDao();
    public abstract DoctorScheduleDao doctorScheduleDao();

    private static volatile DoctorInstaDatabase INSTANCE;
    public static DoctorInstaDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DoctorInstaDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DoctorInstaDatabase.class, "db_doctor_insta")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
