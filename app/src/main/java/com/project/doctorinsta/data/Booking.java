package com.project.doctorinsta.data;

import java.io.Serializable;


public class Booking implements Serializable {


    private Long scheduleId;
    private String patientPhone;
    private String status;

    public Booking() {
    }

    public Booking(String patientPhone, Long scheduleID, String status) {
        this.patientPhone = patientPhone;
        this.scheduleId = scheduleID;
        this.status = status;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
