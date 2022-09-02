package com.example.ambulance_demo;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.ambulance_demo.client.OnlineClient;
import com.example.ambulance_demo.databinding.ActivityMainBinding;
import com.example.ambulance_demo.entity.UserTable;
import com.example.ambulance_demo.service.LocationForeGroundService;
import com.example.ambulance_demo.service.Services;

import android.location.LocationListener;

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
    private String[] permissions;
    Marker marker;
    IMapController mapController;
    List<Overlay> vehicleOverlay;
    ArrayList<OverlayItem> anotherOverlayItemArray;
    private String name, email, id, type;
    LocationManager locationManager;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        this.name = getIntent().getExtras().getString("name");
        this.email = getIntent().getExtras().getString("email");
        this.id = getIntent().getExtras().getString("id");
        this.type = getIntent().getExtras().getString("Type");

        this.permissions = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.FOREGROUND_SERVICE
        };

        if (!hasPermisssion(MainActivity.this, permissions)) {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }

        binding.osmdroid.setTileSource(TileSourceFactory.WIKIMEDIA);
//        binding.osmdroid.setTileSource(TileSourceFactory.MAPNIK);
        binding.osmdroid.setBuiltInZoomControls(true);
        binding.osmdroid.setMultiTouchControls(true);

        vehicleOverlay = this.binding.osmdroid.getOverlays();
        CompassOverlay compassOverlay = new CompassOverlay(MainActivity.this, this.binding.osmdroid);
        compassOverlay.enableCompass();
        vehicleOverlay.add(compassOverlay);
        MyLocationNewOverlay myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(MainActivity.this), this.binding.osmdroid);
        myLocationNewOverlay.enableMyLocation();
        Drawable currentDraw = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_i_am_here, null);
        Bitmap currentIcon = null;
        if (currentDraw != null) {
            currentIcon = ((BitmapDrawable) currentDraw).getBitmap();
        }
        myLocationNewOverlay.setDirectionArrow(currentIcon, currentIcon);
        myLocationNewOverlay.setPersonIcon(currentIcon);
        vehicleOverlay.add(myLocationNewOverlay);

        mapController = binding.osmdroid.getController();
        mapController.setZoom(15);
        GeoPoint startPoint = new GeoPoint(12.962354, 77.533498);
        mapController.setCenter(startPoint);

        this.marker = new Marker(this.binding.osmdroid);

        anotherOverlayItemArray = new ArrayList<OverlayItem>();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MainActivity.this);
        Criteria criteria = new Criteria();
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String bestProvider = locationManager.getBestProvider(criteria, true);
                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                Location location = locationManager.getLastKnownLocation(bestProvider);
                                MainActivity.this.addLocationMarker(location.getLatitude(), location.getLongitude());
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

//                    Call<UserTable[]> call1 = OnlineClient.getInterface().getAllLocation();
//
//                    call1.enqueue(new Callback<UserTable[]>() {
//                        @Override
//                        public void onResponse(Call<UserTable[]> call, Response<UserTable[]> response) {
//
//                            for (UserTable userTable : response.body()) {
//                                MainActivity.this.addVehicleLocation(userTable.getUser_id(), userTable.getLat(), userTable.getLng());
////                                Services.toast(MainActivity.this, "driver:"+userTable.getUser_id());
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserTable[]> call, Throwable t) {
////                            Services.toast(MainActivity.this, t.getLocalizedMessage());
//                        }
//                    });
                }
            }
        }).start();



    }

    public void addLocationMarker(double lat, double lng){
        this.binding.osmdroid.getOverlays().remove(this.marker);
        GeoPoint point = new GeoPoint(lat, lng);
        this.marker.setPosition(point);
        this.marker.setIcon(getResources().getDrawable(R.drawable.ic_i_am_here));
        this.marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        this.binding.osmdroid.getOverlays().add(this.marker);
        this.binding.osmdroid.invalidate();
    }



    public void addVehicleLocation(int id,double lat,double lng){

        OverlayItem item = new OverlayItem("","", new GeoPoint(lat,lng));
        item.setMarker(getResources().getDrawable(R.drawable.ic_ambulance_svgrepo_com));
        anotherOverlayItemArray.add(item);

        ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay
                = new ItemizedIconOverlay<OverlayItem>(
                this, anotherOverlayItemArray, null);

        this.binding.osmdroid.getOverlays().add(anotherItemizedIconOverlay);
    }

    @Override
    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        binding.osmdroid.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        binding.osmdroid.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    private Boolean hasPermisssion(Context context,String[] permissions){
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

//        if(requestCode == 1){
//            Services.toast(MainActivity.this, "Permissions granted");
//        }
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
            startService(intent);
//            Services.toast(MainActivity.this, "service started");
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
        Services.toast(MainActivity.this, location.getLatitude() + "," + location.getLongitude());
    }

    public void quitAmbulance(View v){
        this.stopLocationService();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
//        Services.toast(MainActivity.this, location.getLatitude() + "," + location.getLongitude());
    }
}