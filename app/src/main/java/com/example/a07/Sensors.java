/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 25.07.23, 14:56
 *
 */

package com.example.a07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a07.dao.SensorDao;
import com.example.a07.entity.SensorEntity;
import com.example.a07.utils.Utils;

import java.util.List;

public class Sensors extends AppCompatActivity implements View.OnClickListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean isCounterSensorPresent;
    private Integer stepCount = 0;
    private double threshold = 1.0; // Adjust the threshold as needed
    private double previousAcceleration = 0.0;
    private boolean isStepDetected = false;
    private SensorEventListener stepDetector;
    private TextView textViewStepCounter;
    private EditText editTextInterval;

    private SensorDao sensorDao;
    private Handler handler;
    private Runnable dataStorageRunnable;
    private long storageInterval = 0; // Interval in milliseconds

    private boolean isDataStorageRunning = false; // Flag to track data storage process


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

       // Button backToSetting = findViewById(btn_Totracking);
        findViewById(R.id.btn_setfrequency).setOnClickListener(this);
        findViewById(R.id.btn_stopfrequency).setOnClickListener(this);
        findViewById(R.id.showFuckingSportDatabase).setOnClickListener(this);

        textViewStepCounter = findViewById(R.id.txt_stepCounter);
        editTextInterval = findViewById(R.id.editTextInterval); // Add the EditText field in your layout

        // Initialize SensorManager and Accelerometer Sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Initialize the SensorDao
        sensorDao = MyApplication.getInstance().getSensorDatabase().sensorDoa();

        // Initialize the Step Detector
        stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent != null) {
                    // Retrieve acceleration values
                    float xAcceleration = sensorEvent.values[0];
                    float yAcceleration = sensorEvent.values[1];
                    float zAcceleration = sensorEvent.values[2];

                    // Calculate magnitude and magnitude delta
                    double magnitude = Math.sqrt(xAcceleration * xAcceleration + yAcceleration * yAcceleration + zAcceleration * zAcceleration);
                    double magnitudeDelta = magnitude - previousAcceleration;
                    previousAcceleration = magnitude;

                    // Detect step based on magnitude delta and threshold
                    if (magnitudeDelta > threshold && !isStepDetected) {
                        stepCount++;
                        isStepDetected = true;
                    } else if (magnitudeDelta < threshold) {
                        isStepDetected = false;
                    }

                    // Update step counter text view
                    textViewStepCounter.setText(stepCount.toString());
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Empty implementation
            }
        };

        // Set onClickListener for the backToSetting button
       /* backToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate back to Settings activity
                Intent intent = new Intent();
                intent.setClass(Sensors.this, Tracking.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });*/

        // Register step detector listener
        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume step detector listener
        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister step detector listener
        sensorManager.unregisterListener(stepDetector);
    }

    private void sensorDataToDB() {
        // Create SensorEntity
        SensorEntity sensorEntity = new SensorEntity();
        sensorEntity.setTimeAndDateStamp(Utils.getCurrentDateAndTime());
        sensorEntity.setStepCount(stepCount);

        // Insert SensorEntity into the database
        sensorDao.insert(sensorEntity);
       // showToast("Data Saved");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_setfrequency) {
            // Read the storage interval from the EditText field
            String intervalText = editTextInterval.getText().toString().trim();
            if (!intervalText.isEmpty()) {
                try {
                    // Convert the interval to milliseconds
                    storageInterval = Long.parseLong(intervalText) * 1000; // Convert seconds to milliseconds

                    if (handler != null && dataStorageRunnable != null) {
                        // If there's an existing handler and runnable, remove callbacks
                        handler.removeCallbacks(dataStorageRunnable);
                    }

                    // Create a new Handler and Runnable for data storage
                    handler = new Handler();
                    showToast("Step count data stored every " + storageInterval + " in the database.");
                    dataStorageRunnable = new Runnable() {
                        @Override
                        public void run() {
                            // Store step count data in the database
                            sensorDataToDB();
//                            fuck();
                            // Display toast message
                            // Schedule the next data storage after the storage interval
                            handler.postDelayed(this, storageInterval);

                        }
                    };

                    // Start the data storage process
                    handler.postDelayed(dataStorageRunnable, storageInterval);

                    Intent intent = new Intent();
                    intent.setClass(Sensors.this, Tracking.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } catch (NumberFormatException e) {
                    showToast("Invalid interval format. Please enter a valid number.");
                }
            } else {
                showToast("Please enter an interval.");
            }
        }
            else if (view.getId() == R.id.btn_stopfrequency) {
                // Stop the data storage process and reset the flag
                if (isDataStorageRunning) {
                    handler.removeCallbacks(dataStorageRunnable);
                    isDataStorageRunning = false;
                    showToast("Recording stopped.");
                }
            }

            else if(view.getId() == R.id.showFuckingSportDatabase) {
                try {
                    List<SensorEntity> list = sensorDao.queryAll();
                    if(list == null) {
                        System.out.println("fucking leer");
                    }
                    for (SensorEntity item : list) {
                        Log.d("query_all_tag", item.getTimeAndDateStamp());
                    }
                }catch (Exception e) {
                    Utils.showToast(this, e.getMessage());
                }
        }

    }

    private void fuck() {
        try {
            List<SensorEntity> list = sensorDao.queryAll();
            if(list == null) {
                System.out.println("fucking leer");
            }
            for (SensorEntity item : list) {
                Log.d("query_all_tag", item.getTimeAndDateStamp());
            }
        }catch (Exception e) {
            Utils.showToast(this, e.getMessage());
        }
    }

    // Display toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
