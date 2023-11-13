/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 12:12
 *
 */

package com.example.a07;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a07.dao.GpsDao;
import com.example.a07.entity.GpsEntity;
import com.example.a07.utils.Utils;

public class GPS extends AppCompatActivity implements View.OnClickListener{
    private TextView txtViewLong;
    private TextView txtViewLatit;
    private LocationManager locationManager;
    double latitude;
    double longitude;

    GpsDao gpsDao;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        Button returnToSetting= findViewById(R.id.btn_returnTracking);
        findViewById(R.id.btn_saveGps).setOnClickListener(this);
        gpsDao = MyApplication.getInstance().getGpsDatabase().gpsDao();


        returnToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(GPS.this, Tracking.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE);
        boolean gpsSwitchValue = sharedPreferences.getBoolean("gps", false);




        txtViewLong= findViewById(R.id.txt_long);
        txtViewLatit= findViewById(R.id.txt_latit);
        locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);

        if (gpsSwitchValue) {

            if (ContextCompat.checkSelfPermission(GPS.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(GPS.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(GPS.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    txtViewLong.setText(String.valueOf(longitude));
                    txtViewLatit.setText(String.valueOf(latitude));


                }
            });

        }else
        {
            Utils.showToast(GPS.this, "turn first the gps switch on before saving location");

        }

    }
    private void saveLoctationToDB() {
        // Create a new GpsEntity and set the latitude, longitude, and timestamp
        GpsEntity gpsEntity = new GpsEntity();
        gpsEntity.setLatitude(latitude);
        gpsEntity.setLongitude(longitude);
        gpsEntity.setTimeAndDateStamp(Utils.getCurrentDateAndTime());

        // Insert the GpsEntity into the database using the gpsDao
        gpsDao.insert(gpsEntity);

        Utils.showToast(GPS.this, getString(R.string.location_saved_to_database));
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_saveGps){
            saveLoctationToDB();
        }


    }
}