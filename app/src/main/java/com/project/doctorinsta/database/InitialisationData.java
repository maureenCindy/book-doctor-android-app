package com.project.doctorinsta.database;

import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.DoctorSchedule;
import com.project.doctorinsta.data.Specialisation;
import com.project.doctorinsta.data.User;

public class InitialisationData {


    public static User[] populateUsers() {
        return new User[] {
                new User(100,"Jacob", "Mpofu", "122 Suffolk", "UK"),
                new User(101,"Mary", "Marvel", "2 Blackburn", "USA")
        };
    }

    public static Doctor[] populateDoctors() {
        return new Doctor[] {
                new Doctor(100, "male", 1, "$100/hr"),
                new Doctor(101, "female", 2, "$100/hr")
        };
    }


    public static Specialisation[] populateSpecialities() {
        return new Specialisation[] {
                new Specialisation( 1,"Physiotherapist", "physiotherapist"),
                new Specialisation( 2,"Dermatologist", "Dermatologist"),
                new Specialisation( 3,"Cardiologist", "Cardiologist"),
                new Specialisation( 4,"Dentist", "Dentist"),
                new Specialisation( 5,"General Practitioner", "General Practitioner")
        };
    }


    public static DoctorSchedule[] populateDoctorSchedule() {
        return new DoctorSchedule[] {
                new DoctorSchedule(100,"Mon",5,6),
                new DoctorSchedule(100,"Wed",7,9),
                new DoctorSchedule(100,"Fri",10,12),
                //DOC2
                new DoctorSchedule(101,"Mon",9,10),
                new DoctorSchedule(101,"Tue",11,12)
        };
    }






}
