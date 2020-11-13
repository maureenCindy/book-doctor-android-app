package com.project.doctorinsta.data;

import java.io.Serializable;

public class Doctor implements Serializable {

    private Long specialityIdNumber;
    private Long idNumber;
    private String rate;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String password;
    private String country;
    private String city;
    private String address;
    private String experience;
    public Doctor() {
    }

    public Doctor(String experience,Long idNumber, Long specialityIdNumber, String rate, String firstname, String lastname, String phone,
                  String email, String password, String country, String city, String address) {
       this.experience=experience;
        this.specialityIdNumber = specialityIdNumber;
        this.rate = rate;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.country = country;
        this.city = city;
        this.address = address;
        this.idNumber=idNumber;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Long getSpecialityIdNumber() {
        return specialityIdNumber;
    }

    public void setSpecialityIdNumber(Long specialityIdNumber) {
        this.specialityIdNumber = specialityIdNumber;
    }

    public Long getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(Long idNumber) {
        this.idNumber = idNumber;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
