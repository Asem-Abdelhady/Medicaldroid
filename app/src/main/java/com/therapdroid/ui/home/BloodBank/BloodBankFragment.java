package com.therapdroid.ui.home.BloodBank;

import android.app.Dialog;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.medicaldroid.R;
import com.therapdroid.data.repository.BloodRepository;
import com.therapdroid.data.repository.HospitalRepository;
import com.therapdroid.util.LocalStorageUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static android.support.constraint.Constraints.TAG;
import static com.therapdroid.ui.home.BloodBank.Constants.ERROR_DIALOG_REQUEST;
import static com.therapdroid.ui.home.BloodBank.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.therapdroid.ui.home.BloodBank.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings({"ConstantConditions"})
public class BloodBankFragment extends Fragment implements
        View.OnClickListener,
        OnMapReadyCallback
{

    // UI elements
    private View parent;
    private RelativeLayout secondaryContent;
    private FloatingActionButton createRequestButton;
    private MapView mapView;
    private GoogleMap map;

    // data providers
    private BloodRepository bloodRepository = BloodRepository.getInstance();
    private HospitalRepository hospitalRepository = HospitalRepository.getInstance();
    private LocalStorageUtil localStorageUtil = LocalStorageUtil.getInstance();

    // state
    public MutableLiveData<Boolean> isLocationPermissionGranted = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private boolean loadingState = false;
    private MutableLiveData<Boolean> isBloodRequested = new MutableLiveData<>();
    private LatLng location = null;
    private CompositeDisposable subscriptions;
    private List<Marker> requestsMarkers = new ArrayList<>();
    private List<Marker> hospitalsMarkers = new ArrayList<>();

    public BloodBankFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_blood_bank, container, false);
        return parent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        secondaryContent = parent.findViewById(R.id.blood_bank_secondary_content);
        createRequestButton = parent.findViewById(R.id.blood_bank_create_request_button);
        mapView = parent.findViewById(R.id.blood_bank_map);

        // init the permissions to false to begin with
        isLocationPermissionGranted.setValue(false);
        isLoading.setValue(true);
        isBloodRequested.setValue(localStorageUtil.getRequestId() == null);

        subscriptions = new CompositeDisposable();

        // observe the value of the permissions and initialize the map
        isLocationPermissionGranted.observe(this, value -> {
            if (value) initMap();
        });
        isLoading.observe(this, value -> {
            if (!value) showPrimaryContent();
        });

        createRequestButton.setOnClickListener(this);

        // register event bus listener
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // check if the user had made a request
        if (localStorageUtil.getRequestId() == null) showAddRequestIcon();
        else showDeleteRequestIcon();
        if(checkMapServices()){
            if(!isLocationPermissionGranted.getValue()) getLocationPermission();
        }
    }

    /**
     * This is a high-level function to initiate the whole process of accessing permissions
     * */
    private boolean checkMapServices () {
        if(isServicesOK()) return isMapsEnabled();
        return false;
    }

    /**
     * Prompt the user to a dialog to enable location services
     * */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Check if the location services are enabled on the user's device
     * @return boolean
     * */
    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    /**
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
    public void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {

            isLocationPermissionGranted.setValue(true);

        } else {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            );
        }
    }

    /**
    * Ensure that the user's device has the required version of google play services
     * @return boolean
    * */
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getContext(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void initMap() {
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // start the tracking service
        getContext().startService(new Intent(getContext(), TrackingService.class));

        map.setMyLocationEnabled(true);

        // load blood requests
        subscriptions.add(bloodRepository.loadBloodRequest().subscribe(requests -> {

            if (isLoading.getValue() && loadingState) isLoading.setValue(false);
            loadingState = true;

            // check if the user had made a request
            if (localStorageUtil.getRequestId() == null) showAddRequestIcon();
            else showDeleteRequestIcon();

            deleteMarkers(requestsMarkers);

            Flowable.fromIterable(requests)
                    .map(request -> new MarkerOptions()
                            .position(new LatLng(request.getX(), request.getY()))
                            .title(request.getBlood())
                            .snippet(request.getInfo())
                    )
                    .doOnNext(markerOptions -> requestsMarkers.add(map.addMarker(markerOptions)))
                    .subscribe();

        }, throwable -> {
            throwable.printStackTrace();
            Toast.makeText(getContext(), "You are offline", Toast.LENGTH_SHORT).show();
        }));

        // load hospitals
        subscriptions.add(hospitalRepository.loadHospitals().subscribe(hospitals -> {
            if (isLoading.getValue() && loadingState) isLoading.setValue(false);
            loadingState = true;

            deleteMarkers(hospitalsMarkers);

            Flowable.fromIterable(hospitals)
                    .map(hospital -> new MarkerOptions()
                            .position(new LatLng(hospital.getX(), hospital.getY()))
                            .title(hospital.getName())
                            .snippet(hospital.getManager() +  ", " + hospital.getPhone())
                            .icon(BitmapDescriptorFactory.fromBitmap(
                                    Bitmap.createScaledBitmap(
                                            BitmapFactory.decodeResource(getResources(), R.drawable.hospital_marker),
                                            100,
                                            100,
                                            false)
                            ))
                    )
                    .doOnNext(markerOptions -> hospitalsMarkers.add(map.addMarker(markerOptions)))
                    .subscribe();

        }, throwable -> {
            throwable.printStackTrace();
            Toast.makeText(getContext(), "You are offline", Toast.LENGTH_SHORT).show();
        }));
    }

    /**
     * remove all the markers from the map, and empty the markers list
     */
    private void deleteMarkers (List<Marker> markers) {
        Flowable.fromIterable(markers)
                .doOnNext(Marker::remove)
                .subscribe();
        markers.clear();
    }

    private void showPrimaryContent () {
        secondaryContent.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        subscriptions.dispose();
        subscriptions.clear();
        subscriptions = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.blood_bank_create_request_button) {

            String requestId = localStorageUtil.getRequestId();

            if (requestId == null) {

                Intent intent = new Intent(getContext(), AddRequestActivity.class);
                intent.putExtra("lat", location.latitude);
                intent.putExtra("lng", location.longitude);
                startActivity(intent);

            } else {

                bloodRepository.deleteRequest(requestId).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        createRequestButton.setEnabled(false);
                    }

                    @Override
                    public void onComplete() {
                        createRequestButton.setEnabled(true);
                        localStorageUtil.deleteRequestId();
                        showAddRequestIcon();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "You are offline", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Subscribe
    public void onLocationUpdate (LatLng latLng) {
        location = latLng;
    }

    private void showAddRequestIcon () {
        createRequestButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
    }

    private void showDeleteRequestIcon () {
        createRequestButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_close_white_24dp));
    }

}