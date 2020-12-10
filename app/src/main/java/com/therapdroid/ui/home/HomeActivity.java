package com.therapdroid.ui.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.medicaldroid.R;
import com.therapdroid.ui.home.BloodBank.BloodBankFragment;
import com.therapdroid.ui.home.OnlineDoctor.OnlineDoctorFragment;
import com.therapdroid.ui.home.encyclopedia.EncyclopediaFragment;

import static com.therapdroid.ui.home.BloodBank.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.therapdroid.ui.home.BloodBank.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigationView;
    private TextView fragmentTitle;

    private static final String TAG = "HomeActivity";

    // fragments
    private Fragment
            encyclopediaFragment = new EncyclopediaFragment(),
            emojifierFragment = new EmojifierFragment(),
            bloodBankFragment = new BloodBankFragment(),
            onlineDoctorFragment = new OnlineDoctorFragment(),
            profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigationView = findViewById(R.id.bottom_nav);
        fragmentTitle = findViewById(R.id.fragment_title);

        navigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(bloodBankFragment);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.action_encyclopedia:
                loadFragment(encyclopediaFragment);
                fragmentTitle.setText("Encyclopedia");
                return true;

            case R.id.action_emojifier:
                loadFragment(emojifierFragment);
                fragmentTitle.setText("Emojifier");
                return true;

            case R.id.action_blood_bank:
                loadFragment(bloodBankFragment);
                fragmentTitle.setText("Blood Bank");
                return true;

            case R.id.action_online_doctor:
                loadFragment(onlineDoctorFragment);
                fragmentTitle.setText("Online Doctor");
                return true;

            case R.id.action_profile:
                loadFragment(profileFragment);
                fragmentTitle.setText("Profile");
                return true;

        }

        return false;
    }

    // load fragment into the frame
    private void loadFragment (Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        BloodBankFragment bloodFragment = (BloodBankFragment) bloodBankFragment;
        bloodFragment.isLocationPermissionGranted.setValue(false);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bloodFragment.isLocationPermissionGranted.setValue(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                BloodBankFragment bloodFragment = (BloodBankFragment) bloodBankFragment;
                if(!bloodFragment.isLocationPermissionGranted.getValue()) bloodFragment.getLocationPermission();
            }
        }
    }
}
