package com.project.doctorinsta.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Doctor implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public Long id;
    private int userIdNumber;
    private String gender;
    private int specialisationIdNumber;
    private String rate;
    private int idNumber;

    public Doctor() {
    }

    public Doctor(int idNumber, String gender, int specialisationIdNumber, String rate) {
        this.idNumber =idNumber;
        this.userIdNumber=idNumber;
        this.gender = gender;
        this.specialisationIdNumber = specialisationIdNumber;
        this.rate = rate;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUserIdNumber() {
        return userIdNumber;
    }

    public void setUserIdNumber(int userIdNumber) {
        this.userIdNumber = userIdNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getSpecialisationIdNumber() {
        return specialisationIdNumber;
    }

    public void setSpecialisationIdNumber(int specialisationIdNumber) {
        this.specialisationIdNumber = specialisationIdNumber;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
