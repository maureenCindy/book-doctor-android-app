package com.project.doctorinsta.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.doctorinsta.data.Doctor;
import com.project.doctorinsta.data.Patient;
import com.project.doctorinsta.data.Schedule;
import com.project.doctorinsta.data.Specialisation;

import java.lang.reflect.Type;
import java.util.List;

public class SharedPrefs {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor prefsEditor;
    public static SharedPrefs sharedPrefs;

    private SharedPrefs() {
    }

    public void clearAllPreferences() {
        prefsEditor = sharedPreferences.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }

    public static SharedPrefs getInstance(Context ctx) {
        if (sharedPrefs == null) {
            sharedPrefs = new SharedPrefs();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
            prefsEditor = sharedPreferences.edit();
        }
        return sharedPrefs;
    }

    public void clearPreferences(String key) {
        prefsEditor.remove(key);
        prefsEditor.commit();
    }

    public void setIntValue(String Tag, int value) {
        prefsEditor.putInt(Tag, value);
        prefsEditor.apply();
    }

    public int getIntValue(String Tag) {
        return sharedPreferences.getInt(Tag, 0);
    }

    public void setLongValue(String Tag, long value) {
        prefsEditor.putLong(Tag, value);
        prefsEditor.apply();
    }

    public long getLongValue(String Tag) {
        return sharedPreferences.getLong(Tag, 0);
    }


    public void setValue(String Tag, String token) {
        prefsEditor.putString(Tag, token);
        prefsEditor.commit();
    }

    public String getValue(String Tag) {
        return sharedPreferences.getString(Tag, "----");
    }


    public boolean getBooleanValue(String Tag) {
        return sharedPreferences.getBoolean(Tag, false);

    }

    public void setBooleanValue(String Tag, boolean token) {
        prefsEditor.putBoolean(Tag, token);
        prefsEditor.commit();
    }

    public void setPatient( String tag,Patient patient) {
        Gson gson = new Gson();
        String hashMapString = gson.toJson(patient);
        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }

    public Patient getPatient(String tag) {
            Gson gson = new Gson();
            String storedHashMapString = sharedPreferences.getString(tag, "");
            Type type = new TypeToken<Patient>() {
            }.getType();
            return gson.fromJson(storedHashMapString, type);
    }

    public void setDoctor(String tag, Doctor doctor) {
        Gson gson = new Gson();
        String hashMapString = gson.toJson(doctor);
        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }

    public Doctor getDoctor(String tag) {
        Gson gson = new Gson();
        String storedHashMapString = sharedPreferences.getString(tag, "");
        Type type = new TypeToken<Doctor>() {
        }.getType();
        return gson.fromJson(storedHashMapString, type);
    }

    public void setSpecialities(String tag, List<Specialisation> specialisationList) {
        Gson gson = new Gson();
        String hashMapString = gson.toJson(specialisationList);
        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }

    public List<Specialisation> getSpecialities(String tag) {
        Gson gson = new Gson();
        String storedHashMapString = sharedPreferences.getString(tag, "");
        Type type = new TypeToken<List<Specialisation>>() {
        }.getType();
        return gson.fromJson(storedHashMapString, type);
    }


    public void setAllDoctors(String tag, List<Doctor> doctors) {
        Gson gson = new Gson();
        String hashMapString = gson.toJson(doctors);
        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }

    public List<Doctor> getAllDoctors(String tag) {
        Gson gson = new Gson();
        String storedHashMapString = sharedPreferences.getString(tag, "");
        Type type = new TypeToken<List<Doctor>>() {
        }.getType();
        return gson.fromJson(storedHashMapString, type);
    }




}
