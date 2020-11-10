package com.project.doctorinsta.data;

import java.io.Serializable;


public class Booking implements Serializable {

    private String id;
    private String patient;
    private String scheduleID;
    private String status;

    public Booking() {
    }

    public Booking(String id, String patient, String scheduleID, String status) {
        this.id = id;
        this.patient = patient;
        this.scheduleID = scheduleID;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(String scheduleID) {
        this.scheduleID = scheduleID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
