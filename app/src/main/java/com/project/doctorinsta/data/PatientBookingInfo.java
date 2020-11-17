package com.project.doctorinsta.data;


public class PatientBookingInfo {

    private String patient;
    private String phone;
    private String date;
    private String start;
    private String end;

    public PatientBookingInfo(String patient, String phone, String date, String start, String end) {
        this.patient = patient;
        this.phone = phone;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
}
