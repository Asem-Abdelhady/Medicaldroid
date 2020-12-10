package com.therapdroid.ui.home.BloodBank;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.medicaldroid.R;

import org.greenrobot.eventbus.EventBus;

public class TrackingService extends Service {

    public TrackingService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildNotification();
        requestLocationUpdates();
    }

    /**
     * Create the persistent notification
     */
    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the persistent notification
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("We are tracking your location")

                //Make this notification ongoing so it can’t be dismissed by the user//

                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.ic_blood_bank_24dp);
        startForeground(1, builder.build());
    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //Unregister the BroadcastReceiver when the notification is tapped
            unregisterReceiver(stopReceiver);

            // stop the service
            stopSelf();
        }
    };

    private void requestLocationUpdates () {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        // TODO: Emit the data for observers
                        EventBus.getDefault().post(new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                }
            }, null);
        }
    }
}
