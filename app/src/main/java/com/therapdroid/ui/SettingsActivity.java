package com.therapdroid.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

import com.medicaldroid.R;
import com.therapdroid.model.util.Settings;
import com.therapdroid.util.LocalStorageUtil;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private SwitchCompat notifications;

    private LocalStorageUtil localStorageUtil;

    private Settings dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        notifications = findViewById(R.id.settings_notifications);
        notifications.setOnCheckedChangeListener(this);

        localStorageUtil = LocalStorageUtil.getInstance();

        dataModel = localStorageUtil.getSettings();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
