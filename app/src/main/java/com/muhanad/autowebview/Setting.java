package com.muhanad.autowebview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class Setting {
    public static final String PREF_NAME = "com.example";
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    // Constructor
    @SuppressLint("CommitPrefEdits")
    public Setting(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public String getKey(String name) {
        return pref.getString(name, "");
    }

    public void setKey(String name, String value) {
        editor.putString(name, value);
        editor.commit();
    }

    public void setURL(String url) {
        editor.putString("url", url);
        editor.commit();
    }

    public String getURL() { return pref.getString("url", "https://www.aljazeera.net/");}

}
