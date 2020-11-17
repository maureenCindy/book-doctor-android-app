package com.project.doctorinsta.data;


import java.io.Serializable;

public class PatientBooking implements Serializable {

    private String patientPhone;
    private Long scheduleID;
    private String date;
    private String startTime;
    private String endTime;
    private String status;

    public PatientBooking(String patientPhone, Long scheduleID, String date, String startTime, String endTime, String status) {
        this.patientPhone = patientPhone;
        this.scheduleID = scheduleID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }


    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public Long getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(Long scheduleID) {
        this.scheduleID = scheduleID;
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
