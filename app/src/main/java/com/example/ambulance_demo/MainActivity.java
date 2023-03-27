package com.example.ambulance_demo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.ambulance_demo.adapter.MyAlertAdapter;
import com.example.ambulance_demo.client.OnlineClient;
import com.example.ambulance_demo.databinding.ActivityMainBinding;
import com.example.ambulance_demo.entity.AlertTable;
import com.example.ambulance_demo.entity.UserTable;
import com.example.ambulance_demo.service.LocationForeGroundService;
import com.example.ambulance_demo.service.Services;

import android.location.LocationListener;

import com.example.ambulance_demo.viewmodel.MainActivityViewModel;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private ActivityMainBinding binding;
    Marker marker;
    IMapController mapController;
    List<Overlay> vehicleOverlay;
    ArrayList<OverlayItem> anotherOverlayItemArray;
    private String name, email, user_type;
    private int id, alert_id = 0, alert_id_pick = 0, alert_user_id;
    LocationManager locationManager;
    ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay;
    private MyAlertAdapter myAlertAdapter;
    private MyLocationNewOverlay myLocationNewOverlay;
    private MainActivityViewModel mainActivityViewModel;


    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.binding.recycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        this.name = getIntent().getExtras().getString("name");
        this.email = getIntent().getExtras().getString("email");
        this.id = getIntent().getExtras().getInt("id");
        this.user_type = getIntent().getExtras().getString("type");

        String[] permissions = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.FOREGROUND_SERVICE
        };

        if (!hasPermission(MainActivity.this, permissions)) {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }

//        binding.osmdroid.setTileSource(TileSourceFactory.WIKIMEDIA);
        binding.osmdroid.setTileSource(TileSourceFactory.MAPNIK);
        binding.osmdroid.setBuiltInZoomControls(true);
        binding.osmdroid.setMultiTouchControls(true);

        vehicleOverlay = this.binding.osmdroid.getOverlays();
        CompassOverlay compassOverlay = new CompassOverlay(MainActivity.this, this.binding.osmdroid);
        compassOverlay.enableCompass();
        binding.osmdroid.getOverlays().add(compassOverlay);

        myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(MainActivity.this), this.binding.osmdroid);
        myLocationNewOverlay.enableMyLocation();
        binding.osmdroid.getOverlays().add(myLocationNewOverlay);

        mapController = binding.osmdroid.getController();
        mapController.setZoom(15);
        myLocationNewOverlay.runOnFirstFix(() -> {
            final GeoPoint myLocation = myLocationNewOverlay.getMyLocation();
            if (myLocation != null) {
                MainActivity.this.runOnUiThread(() -> {
                    mapController.setCenter(myLocationNewOverlay.getMyLocation());
                    mapController.animateTo(myLocationNewOverlay.getMyLocation());
                });
            };
        });

        anotherOverlayItemArray = new ArrayList<>();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MainActivity.this);

        mainActivityViewModel = ViewModelProviders.of(MainActivity.this).get(MainActivityViewModel.class);

        new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(3000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                if(this.user_type.equals("ambulance")){
                    MainActivity.this.mainActivityViewModel.setData(OnlineClient.getInterface().getAllAlert(), MainActivity.this.mainActivityViewModel.alerts_data);
                    if(this.alert_id_pick != 0){
                        this.mainActivityViewModel.setData(OnlineClient.getInterface().getAlertUserPicker(alert_id_pick), this.mainActivityViewModel.alert_user_rejection);
                    }
                }
                else {
                    MainActivity.this.mainActivityViewModel.setData(OnlineClient.getInterface().getVehicles(), MainActivity.this.mainActivityViewModel.vehicles_data);
                    if(this.alert_id != 0){
                        MainActivity.this.mainActivityViewModel.setData(OnlineClient.getInterface().getAlertPicker(MainActivity.this.alert_id), MainActivity.this.mainActivityViewModel.alert_picker_details_for_user);
                        MainActivity.this.mainActivityViewModel.setData(OnlineClient.getInterface().getAlertPickerUser(this.alert_id), MainActivity.this.mainActivityViewModel.alert_picker_rejection);
                    }
                }
            }
        }).start();

        //get all the alerts and display to driver
        this.mainActivityViewModel.alerts_data.observeForever(alertTables -> {
            if(alertTables != null){
                myAlertAdapter = new MyAlertAdapter(alertTables, MainActivity.this);
                MainActivity.this.binding.recycleView.setAdapter(myAlertAdapter);
                myAlertAdapter.notifyDataSetChanged();
            }
        });
        //get all the vehicles and display to user
        this.mainActivityViewModel.vehicles_data.observeForever(userTables -> {
            if(userTables != null){
                MainActivity.this.binding.osmdroid.getOverlays().remove(anotherItemizedIconOverlay);
                anotherOverlayItemArray.clear();
                for(UserTable userTable: userTables){
                    MainActivity.this.addVehicleLocation(userTable.getUser_id(), userTable.getLat(), userTable.getLng());
                }
            }
        });
        //user triggered the alert button
        this.mainActivityViewModel.alert_from_user.observeForever(alertTable -> {
            if(alertTable != null){
                this.alert_id = alertTable.getAlert_id();
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.address, "");
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.quit, View.VISIBLE);
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.call, View.INVISIBLE);
            }
        });
        //get the alert picker when user alert the ambulance
        this.mainActivityViewModel.alert_picker_details_for_user.observeForever(userTable -> {
            if(userTable != null){
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.name, "Ambulance Driver Name:"+ userTable.getUser_name());
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.phone, "Ambulance Driver Phone No:" + userTable.getUser_phone());
            }
            else{
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.name, "Ambulance Driver Name:");
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.phone, "Ambulance Driver Phone No:" );
            }
        });
        //get the driver name and display to user
        this.mainActivityViewModel.name.observeForever(s -> {
            if(s != null) this.binding.name.setText(s);
        });
        //get the driver phone no and display to user
        this.mainActivityViewModel.phone.observeForever(s ->{
            if(s != null) this.binding.phoneNo.setText(s);
        });
        //user triggered cancel button of the alert
        this.mainActivityViewModel.alert_error_cancel_from_user.observeForever(errorTable -> {
            if(errorTable != null){
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.alert_picker_details_for_user, null);
                MainActivity.this.binding.quit.setVisibility(View.INVISIBLE);
                MainActivity.this.binding.call.setVisibility(View.VISIBLE);
                Services.toast(MainActivity.this, errorTable.getErr_msg());
            }
        });
        //accept the alert and get the user location
        this.mainActivityViewModel.alert_accept_from_driver.observeForever(alertTable -> {
            if(alertTable != null){
                    this.mainActivityViewModel.setValue(this.mainActivityViewModel.address, "Passenger Pickup Point:" + alertTable.getAlert_user_lat()+","+ alertTable.getAlert_user_lng());
                    this.mainActivityViewModel.setValue(this.mainActivityViewModel.recycleView, View.INVISIBLE);
                    this.mainActivityViewModel.setData(OnlineClient.getInterface().getAlertUser(this.alert_user_id), this.mainActivityViewModel.alert_user_details_for_picker);
            }
        });
        this.mainActivityViewModel.alert_user_rejection.observeForever(alertTable -> {
            if(alertTable != null){
                if(alertTable.getAlert_status() == 3) {
                    this.mainActivityViewModel.setValue(this.mainActivityViewModel.address, "User reject the alert message");
                    this.mainActivityViewModel.setValue(this.mainActivityViewModel.alert_user_details_for_picker, null);
                }
            }

        });
        //accept the alert and get the user details
        this.mainActivityViewModel.alert_user_details_for_picker.observeForever(userTable -> {
            if(userTable != null){
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.name,"Passenger Name:" + userTable.getUser_name());
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.phone, "Passenger Phone No:" + userTable.getUser_phone());
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.reject, View.VISIBLE);
            }
            else{
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.name,"Passenger Name:" );
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.phone, "Passenger Phone No:");
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.reject, View.INVISIBLE);
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.recycleView, View.VISIBLE);
            }
        });
        //driver reject the alert and change the user information
        this.mainActivityViewModel.alert_error_reject_from_driver.observeForever(errorTable -> {
            if(errorTable != null){
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.reject, View.INVISIBLE);
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.recycleView, View.VISIBLE);
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.name, "Passenger Name:");
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.phone, "Passenger Phone No:");
                this.mainActivityViewModel.setValue(this.mainActivityViewModel.address, "Passenger Pickup Point:");

            }
        });
        this.mainActivityViewModel.alert_picker_rejection.observeForever(alertTable -> {
            if(alertTable != null){
                if(alertTable.getAlert_status() == 4){
                    this.mainActivityViewModel.setValue(this.mainActivityViewModel.name, "Ambulance Driver Name:");
                    this.mainActivityViewModel.setValue(this.mainActivityViewModel.phone, "Ambulance Driver Phone No:");
                    this.mainActivityViewModel.setValue(this.mainActivityViewModel.address, "Driver Reject the Alert, Please call again");
                    this.mainActivityViewModel.setValue(this.mainActivityViewModel.quit, View.INVISIBLE);
                    this.mainActivityViewModel.setValue(this.mainActivityViewModel.call, View.VISIBLE);
                }
            }
        });

        this.mainActivityViewModel.call.observeForever(s -> {
            if (s != null) this.binding.call.setVisibility(s);
        });

        this.mainActivityViewModel.quit.observeForever(s -> {
            if (s != null) this.binding.quit.setVisibility(s);
        });

        this.mainActivityViewModel.reject.observeForever(s -> {
            if (s != null) this.binding.reject.setVisibility(s);
        });

        this.mainActivityViewModel.recycleView.observeForever(s -> {
            if (s != null) this.binding.recycleView.setVisibility(s);
        });

        this.mainActivityViewModel.address.observeForever(s -> {
            if (s != null) this.binding.address.setText(s);
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(this.user_type.equals("ambulance")) {
            this.mainActivityViewModel.setValue(this.mainActivityViewModel.quit, View.INVISIBLE);
            this.mainActivityViewModel.setValue(this.mainActivityViewModel.reject, View.INVISIBLE);
            this.mainActivityViewModel.setValue(this.mainActivityViewModel.quit, View.INVISIBLE);
            this.mainActivityViewModel.setValue(this.mainActivityViewModel.call, View.INVISIBLE);
            this.mainActivityViewModel.setValue(this.mainActivityViewModel.name, "Passenger Name:");
            this.mainActivityViewModel.setValue(this.mainActivityViewModel.phone, "Passenger Phone No:");
            this.mainActivityViewModel.setValue(this.mainActivityViewModel.address, "Passenger Pickup Point:");
            startLocationService();
        }
        else{
            this.mainActivityViewModel.setValue(this.mainActivityViewModel.quit, View.INVISIBLE);
            this.mainActivityViewModel.setValue(this.mainActivityViewModel.reject, View.INVISIBLE);
            this.mainActivityViewModel.setValue(this.mainActivityViewModel.recycleView, View.INVISIBLE);
            this.mainActivityViewModel.setValue(this.mainActivityViewModel.name, "Ambulance Driver Name:");
            this.mainActivityViewModel.setValue(this.mainActivityViewModel.phone, "Ambulance Driver Phone No:");
            this.mainActivityViewModel.setValue(this.mainActivityViewModel.address, "");
        }
    }



    public Bitmap icon(Drawable drawable){
        try {
            Bitmap bitmap;

            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            // Handle the error
            return null;
        }
    }

//    public void addLocationMarker(double lat, double lng){
//        this.binding.osmdroid.getOverlays().remove(this.marker);
//        GeoPoint point = new GeoPoint(lat, lng);
//        this.marker.setPosition(point);
//        this.marker.setIcon(getResources().getDrawable(R.drawable.ic_i_am_here));
//        this.marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        this.binding.osmdroid.getOverlays().add(this.marker);
//        this.binding.osmdroid.invalidate();
//    }



    @SuppressLint("UseCompatLoadingForDrawables")
    public void addVehicleLocation(int id, double lat, double lng){
        OverlayItem item = new OverlayItem("","", new GeoPoint(lat,lng));
        item.setMarker(getResources().getDrawable(R.drawable.ic_ambulance_svgrepo_com));
        anotherOverlayItemArray.add(item);
        anotherItemizedIconOverlay = new ItemizedIconOverlay<>(this, anotherOverlayItemArray, null);
        this.binding.osmdroid.getOverlays().add(anotherItemizedIconOverlay);
    }


    @Override
    public void onResume(){
        super.onResume();
        binding.osmdroid.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        binding.osmdroid.onPause();
    }

    private Boolean hasPermission(Context context, String[] permissions){
        for(String permission : permissions){
            if(ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean isLocationServiceRunning(){
        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null){
            for(ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(LocationServices.class.getName().equals(serviceInfo.service.getClassName())){
                    if(serviceInfo.foreground){
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(), LocationForeGroundService.class);
            intent.setAction(LocationForeGroundService.ACTION_START_LOCATION_SERVICE);
            intent.putExtra("email", this.email);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            }
            else{
                startService(intent);
            }
        }
    }

    private void stopLocationService(){
        Services.toast(MainActivity.this, "service ended");
        Intent intent = new Intent(getApplicationContext(), LocationForeGroundService.class);
        intent.setAction(LocationForeGroundService.ACTION_STOP_LOCATION_SERVICE);
        intent.putExtra("email", this.email);
        startService(intent);
    }

    @SuppressLint("MissingPermission")
    public void callAmbulance(View v){
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        this.mainActivityViewModel.setData(OnlineClient.getInterface().alertUser(new AlertTable(this.id, location.getLatitude(), location.getLongitude())), this.mainActivityViewModel.alert_from_user);
    }

    public void quitAmbulance(View v){
        this.mainActivityViewModel.setData(OnlineClient.getInterface().cancelAlertUser(new AlertTable(MainActivity.this.alert_id)), this.mainActivityViewModel.alert_error_cancel_from_user);
    }

    public void acceptAmbulance(int alert_id, int alert_user_id){
        this.alert_id_pick = alert_id;
        this.alert_user_id = alert_user_id;
        this.mainActivityViewModel.setData(OnlineClient.getInterface().getAcceptAlert(new AlertTable(alert_id, id)), this.mainActivityViewModel.alert_accept_from_driver);
    }

    public void rejectAccept(View view){
        this.mainActivityViewModel.setData(OnlineClient.getInterface().rejectUser(new AlertTable(this.alert_id_pick)), this.mainActivityViewModel.alert_error_reject_from_driver);
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}