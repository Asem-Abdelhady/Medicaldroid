package com.therapdroid.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.therapdroid.model.database.User;
import com.therapdroid.model.util.Settings;

public class LocalStorageUtil {
    private static LocalStorageUtil ourInstance = null;

    private SharedPreferences sharedPreferences;
    private Gson parser;

    /* Constructor */
    private LocalStorageUtil(Context context) {
        sharedPreferences = context.getSharedPreferences("medicaldroid", Context.MODE_PRIVATE);
        parser = new Gson();
    }

    public static void init (Context context) {
        ourInstance = new LocalStorageUtil(context);
    }

    public static LocalStorageUtil getInstance() {
        return ourInstance;
    }

    public void saveRequestId (String id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("requestId", id);
        editor.apply();
    }

    public String getRequestId () {
        return sharedPreferences.getString("requestId", null);
    }

    public void deleteRequestId () {
        sharedPreferences.edit().remove("requestId").apply();
    }

    public void clear () {
        sharedPreferences.edit().clear().apply();
    }

    public Settings getSettings () {
        return parser.fromJson(sharedPreferences.getString("settings", null), Settings.class);
    }

}
