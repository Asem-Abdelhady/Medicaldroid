package com.therapdroid;

import android.app.Application;

import com.therapdroid.util.LocalStorageUtil;

import io.realm.Realm;

public class MedicalDroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        LocalStorageUtil.init(this);
    }
}
