package com.project.doctorinsta;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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


}
