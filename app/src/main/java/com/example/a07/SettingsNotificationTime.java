/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 08:13
 *
 */

package com.example.a07;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class SettingsNotificationTime extends AppCompatActivity implements View.OnClickListener {
    private TimePicker timePicker;
    private TextView timePicker_result1;
    private TextView timePicker_result2;
    private TextView timePicker_result3;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_notification_time);

        sharedPreferences = getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        timePicker_result1 = findViewById(R.id.text_timePicker_result1);
        timePicker_result1.setText(sharedPreferences.getString("time1", ""));
        timePicker_result2 = findViewById(R.id.text_timePicker_result2);
        timePicker_result2.setText(sharedPreferences.getString("time2", ""));
        timePicker_result3 = findViewById(R.id.text_timePicker_result3);
        timePicker_result3.setText(sharedPreferences.getString("time3", ""));

        MaterialButton returnSettings = findViewById(R.id.btn_returnSettings);

        //set onClickListeners for buttons
        findViewById(R.id.btn_timeOK1).setOnClickListener(this);
        findViewById(R.id.btn_timeOk2).setOnClickListener(this);
        findViewById(R.id.btn_timeOK3).setOnClickListener(this);

        returnSettings.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(SettingsNotificationTime.this, Settings.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        // set up notification channel
        CharSequence name = getString(R.string.notification_channel);
        String description = getString(R.string.questionnaire_notifications);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("questionnaireNotification", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id  == R.id.btn_timeOK1){
            if ((timePicker.getHour() >= 7 && timePicker.getHour() < 11) || (timePicker.getHour() == 11 && timePicker.getMinute() == 0)) {
                String desc = String.format(Locale.getDefault(), "%s:%s", String.format(Locale.getDefault(), "%02d", timePicker.getHour()), String.format(Locale.getDefault(), "%02d",timePicker.getMinute()));
                timePicker_result1.setText(desc);
                saveTime(desc, 1);
            }
            else{
                Toast.makeText(this, R.string.please_choose_a_time_between_7_00_and_11_00_for_the_first_notification, Toast.LENGTH_SHORT).show();
            }
        }
        if (id  == R.id.btn_timeOk2) {
            if ((timePicker.getHour() >= 12 && timePicker.getHour() < 14) || (timePicker.getHour() == 14 && timePicker.getMinute() == 0)) {
                String desc = String.format(Locale.getDefault(), "%s:%s", String.format(Locale.getDefault(), "%02d", timePicker.getHour()), String.format(Locale.getDefault(), "%02d", timePicker.getMinute()));
                timePicker_result2.setText(desc);
                saveTime(desc, 2);
            }
            else{
                Toast.makeText(this, R.string.please_choose_a_time_between_12_00_and_14_00_for_the_second_notification, Toast.LENGTH_SHORT).show();
            }
        }
        if (id  == R.id.btn_timeOK3){
            if ((timePicker.getHour() >= 17 && timePicker.getHour() < 19) || (timePicker.getHour() == 19 && timePicker.getMinute() == 0)) {
                String desc = String.format(Locale.getDefault(), "%s:%s", String.format(Locale.getDefault(), "%02d", timePicker.getHour()), String.format(Locale.getDefault(), "%02d", timePicker.getMinute()));
                timePicker_result3.setText(desc);
                saveTime(desc, 3);
            }
            else{
                Toast.makeText(this, R.string.please_choose_a_time_between_17_00_and_19_00_for_the_third_notification, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // save time to shared preferences
    public void saveTime(String time, int id) {
        editor.putString(getString(R.string.time)+id, time);
        editor.apply();

        // Parse hour and minute from the time string
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        // Create an intent to your BroadcastReceiver
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("notification_id", id);  // Pass id to BroadcastReceiver

        // Create a PendingIntent that will perform a broadcast
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_IMMUTABLE);

        // Check if the alarm is already set
        if (PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE) != null) {
            Log.d("Alarm", "Alarm with id " + id + " is already set. Updating time to " + hour + ":" + minute + ".");
        } else {
            Log.d("Alarm", "Alarm with id " + id + " is not set. Setting for the first time. Time: " + hour + ":" + minute + ".");
        }

        // Get an instance of AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Get current time in milliseconds
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        // Set the alarm
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}