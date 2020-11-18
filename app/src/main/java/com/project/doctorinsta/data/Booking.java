package com.project.doctorinsta.data;

import java.io.Serializable;


public class Booking implements Serializable {


    private Long doctorIdNumber;
    private Long scheduleId;
    private String patientPhone;
    private String status;

    public Booking(Long doctorIdNumber, Long scheduleId, String patientPhone, String status) {
        this.doctorIdNumber = doctorIdNumber;
        this.scheduleId = scheduleId;
        this.patientPhone = patientPhone;
        this.status = status;
    }

    public Booking() {
    }

    public Long getDoctorIdNumber() {
        return doctorIdNumber;
    }

    public void setDoctorIdNumber(Long doctorIdNumber) {
        this.doctorIdNumber = doctorIdNumber;
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
