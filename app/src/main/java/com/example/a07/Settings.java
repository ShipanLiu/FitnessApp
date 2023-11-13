/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 12:12
 *
 */

package com.example.a07;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_POST_NOTIFICATIONS = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MaterialButton toNotification = findViewById(R.id.btn_setNotification);
        SwitchMaterial gpsSwitch = findViewById(R.id.gpsSwitch);

        sharedPreferences = getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        // set the gps switch to the saved value
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            gpsSwitch.setChecked(sharedPreferences.getBoolean("gps", false));
        }
        else {
            gpsSwitch.setChecked(false);
            editor.putBoolean("gps", gpsSwitch.isChecked());
            editor.apply();
            Log.d("GPS", "Updated Shared Preferences - GPS Status: " + sharedPreferences.getBoolean("gps", false));
        }

        // Set the gps switch listener
        gpsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Check for ACCESS_FINE_LOCATION permission
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && isChecked) {

                // Permission is not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(this, R.string.we_need_your_location_to_provide_better_service, Toast.LENGTH_LONG).show();
                }
                // Request permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                // If permission has already been granted save the gps setting in shared preferences
                editor.putBoolean("gps", isChecked);
                editor.apply();
                Log.d("GPS", "Updated Shared Preferences - GPS Status: " + sharedPreferences.getBoolean("gps", false));
            }
        });

        // Set the notification button listener
        toNotification.setOnClickListener(view -> {
            // Only request POST_NOTIFICATIONS permission for SDK 33 and above
            if (android.os.Build.VERSION.SDK_INT >= 33) {
                // Check and request for POST_NOTIFICATIONS permission
                if (ContextCompat.checkSelfPermission(view.getContext(),
                        Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) view.getContext(),
                            Manifest.permission.POST_NOTIFICATIONS)) {
                        Toast.makeText(view.getContext(), R.string.we_need_your_permission_to_send_notifications, Toast.LENGTH_LONG).show();
                    }
                    // Request permission
                    ActivityCompat.requestPermissions((Activity) view.getContext(),
                            new String[]{Manifest.permission.POST_NOTIFICATIONS},
                            MY_PERMISSIONS_REQUEST_POST_NOTIFICATIONS);
                } else {
                    // Permission has already been granted
                    openActivity(SettingsNotificationTime.class);
                }
            }
            else {
                // SDK 32 and below don't need POST_NOTIFICATIONS permission - just open the activity
                openActivity(SettingsNotificationTime.class);
            }
        });
    }
  
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SwitchMaterial gpsSwitch = findViewById(R.id.gpsSwitch);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Toast.makeText(this, R.string.location_permission_granted, Toast.LENGTH_SHORT).show();
                }
                else {
                    // permission denied
                    Toast.makeText(this, R.string.location_permission_denied_please_enable_exact_position_for_the_app_in_the_settings, Toast.LENGTH_SHORT).show();
                    gpsSwitch.setChecked(false);
                }
                editor.putBoolean("gps", gpsSwitch.isChecked());
                editor.apply();
                Log.d("GPS", "Updated Shared Preferences - GPS Status: " + sharedPreferences.getBoolean("gps", false));
                return;
            }
            case MY_PERMISSIONS_REQUEST_POST_NOTIFICATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Toast.makeText(this, R.string.notification_permission_granted, Toast.LENGTH_SHORT).show();
                    openActivity(SettingsNotificationTime.class);
                } else {
                    // permission denied
                    Toast.makeText(this, R.string.notification_permission_denied_please_allow_notifications_to_receive_a_reminder_for_the_questionnaire, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Method to do something when an item is selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();

        // Change app language based on selection
        switch (selectedItem) {
            case "SensorData":
                openActivity(Sensors.class);
                break;
            case "GpsData":
                openActivity(GPS.class);
                break;
            default:
                // Do nothing
                break;
        }
    }

    // Implement the onNothingSelected method to do something when no item is selected
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }


    // Methods to switch activity
    public void switchActivity(View view) {
        String tag = view.getTag().toString();
        try {
            Class<?> activityClass = Class.forName(getPackageName() + "." + tag);
            openActivity(activityClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}