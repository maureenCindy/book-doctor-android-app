package com.project.doctorinsta.data;

import java.io.Serializable;


public class BookingInfo implements Serializable {


    private String doctor;
    private String specialty;
    private String date;
    private String start;
    private String end;
    private String address;

    public BookingInfo(String doctor, String specialty, String date, String start, String end, String address) {
        this.doctor = doctor;
        this.specialty = specialty;
        this.date = date;
        this.start = start;
        this.end = end;
        this.address = address;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
