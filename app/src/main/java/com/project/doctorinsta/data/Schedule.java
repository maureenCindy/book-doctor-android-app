package com.project.doctorinsta.data;

import java.io.Serializable;

public class Schedule implements Serializable {

    private Long id;
    private Long doctorIdNumber;
    private String date;
    private String startTime;
    private String endTime;
    private String status;

    public Schedule() {
    }

    public Schedule(Long id, Long doctorIdNumber, String date, String startTime, String endTime, String status) {
        this.id = id;
        this.doctorIdNumber = doctorIdNumber;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", doctorIdNumber=" + doctorIdNumber +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
