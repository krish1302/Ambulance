package com.example.ambulance_demo.service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.ambulance_demo.client.OnlineClient;
import com.example.ambulance_demo.entity.ErrorTable;
import com.example.ambulance_demo.entity.UserTable;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import retrofit2.Call;

public class LocationForeGroundService extends Service {

    public static final int LOCATION_SERVICE = 175;
    public static final String ACTION_START_LOCATION_SERVICE = "startLocationService";
    public static final String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";
    private String email;
    public double longitude, latitude;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if(locationResult != null && locationResult.getLastLocation() !=null){
                double lat = locationResult.getLastLocation().getLatitude();
                double lng = locationResult.getLastLocation().getLongitude();
                if(latitude == 0 && longitude == 0){
                    latitude = lat;
                    longitude = lng;
                    Log.d("Foreground 1", latitude +","+longitude);
                    Call<ErrorTable> call= OnlineClient.getInterface().createLocation(new UserTable(email, lat, lng));
                    Services.call(call, getApplicationContext());
                }
                else if(lat == latitude && lng == longitude){
//                    Services.toast(getApplicationContext(), "Same location no change");
                }
                else{
                    Log.d("Foreground", lat +","+lng);
                    latitude = lat;
                    longitude = lng;
                    Call<ErrorTable> call= OnlineClient.getInterface().createLocation(new UserTable(email, lat, lng));
                    Services.call(call, getApplicationContext());
                }

            }

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented...");
    }

    @SuppressLint("MissingPermission")
    private void startLocationService(){
        String channel_id = "ambulance_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(
                    getApplicationContext(),
                    0,
                    resultIntent,
                    PendingIntent.FLAG_IMMUTABLE
            );
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                getApplicationContext(),
                channel_id
        );
        notificationBuilder.setContentTitle("Location service for Ambulance app");
        notificationBuilder.setContentText("Make sure turned on");
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        notificationBuilder.setAutoCancel(false);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(notificationManager != null
                    && notificationManager.getNotificationChannel(channel_id) == null){
                NotificationChannel notificationChannel = new NotificationChannel(
                        channel_id,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("This channel is used by location service");
                notificationManager.createNotificationChannel(notificationChannel);

            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());


        startForeground(LOCATION_SERVICE,notificationBuilder.build());
    }


    private void stopLocationService(){;
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            String action = intent.getAction();
            this.email = intent.getStringExtra("email");

            if(action != null){
                if(action.equals(ACTION_START_LOCATION_SERVICE)){
                    startLocationService();
                }
                else if(action.equals(ACTION_STOP_LOCATION_SERVICE)){
                    stopLocationService();
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
