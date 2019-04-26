package com.example.teamball;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreference {
    private static final String TAG = "SharedPreference";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SharedPreference(Activity activity) {
        super();

        Log.d(TAG, "SharedPreference: C'tor");
        prefs = activity.getSharedPreferences(activity.getString(R.string.prefsFileKey), Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void save(String key, String text) {
        Log.d(TAG, "save: " + key + ":" + text);
        editor.putString(key, text);
        editor.apply(); // commit T/F
    }


    public String getValue(String key) {
        String text = prefs.getString(key, "");
        Log.d(TAG, "getValue: " + key + " = " + text);
        return text;
    }


    public void clearAll() {
        Log.d(TAG, "clearAll: ");
        editor.clear();
        editor.apply(); // commit T/F
    }
}
