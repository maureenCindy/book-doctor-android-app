package com.project.doctorinsta.data;

import java.io.Serializable;

public class Schedule implements Serializable {

    private Long doctorIdNumber;
    private String date;
    private String startTime;
    private String endTime;
    private String status;

    public Schedule() {
    }

    public Schedule( Long doctorIdNumber, String date, String startTime, String endTime, String status) {
        this.doctorIdNumber = doctorIdNumber;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }


    public Long getDoctorIdNumber() {
        return doctorIdNumber;
    }

    public void setDoctorIdNumber(Long doctorIdNumber) {
        this.doctorIdNumber = doctorIdNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
