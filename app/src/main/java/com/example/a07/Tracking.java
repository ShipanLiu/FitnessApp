/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 15:27
 *
 */

package com.example.a07;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a07.dao.SensorDao;
import com.example.a07.dao.SportDao;
import com.example.a07.entity.QuestionaireEntity;
import com.example.a07.entity.SensorEntity;
import com.example.a07.entity.SportEntity;
import com.example.a07.utils.SharedPreferencesUtil;
import com.example.a07.utils.Utils;

import java.util.Calendar;
import java.util.List;

public class Tracking extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {

    private SportDao sportDao;
//    private SensorDao sensorDao;

    //declare attributes for sportpage
    private Spinner sportTypeSpinner;
    private TextView currentDate;
    private Calendar calendar;
    private Chronometer chronometer;


    //for the chronometer
    private double timeRecorded = 0;
    private double timeRecordedMinute = 0;
    private boolean isRunning = false;


    //for the spinner
    private String sportName;

    //the mood score
    private int myMoodScore = 50; // moodscore after sport

    // Daeun - for providing reward
    private int goal = 0;
    private int healthCoin = 0;

    // Daeun - for recording reward
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    private final static String[] MySportArray = {"Wandering", "Jogging", "Swimming", "Ball sports", "Weight training", "Yoga", "Climbing/Bouldering","Others" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_tracking);
        findViewById(R.id.btn_sport_queryAll).setOnClickListener(this);
        findViewById(R.id.btn_reset_firstopen).setOnClickListener(this);
//        findViewById(R.id.showFuckingSportDatabase).setOnClickListener(this);
        // get sportDao
        sportDao = MyApplication.getInstance().getSportDatabase().sportDao();
//        sportDao = MyApplication.getInstance().getSportDatabase().();


        // Minhua
        //connect attributes with layout elements
        sportTypeSpinner = findViewById(R.id.spinnerSportType);
        currentDate = findViewById(R.id.tvShowCurrentDate);

        //sensorspinner
        Spinner sensorSpinner= findViewById(R.id.sensorSpinner);
        ArrayAdapter<CharSequence> sensorAdapter = ArrayAdapter.createFromResource(this,
                R.array.sensor_array, android.R.layout.simple_spinner_item);
        sensorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sensorSpinner.setAdapter(sensorAdapter);
        sensorSpinner.setOnItemSelectedListener(this);


        //set current date
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        currentDate.setText(day + "." + month + "." + year);

        //set buttons with onClickListeners
        findViewById(R.id.btnSportStart).setOnClickListener(this);
        findViewById(R.id.btnSportStop).setOnClickListener(this);
//      findViewById(R.id.btnAddSport).setOnClickListener(this);

        //set chronometer
        chronometer = findViewById(R.id.chronometerSport);


        //set listener for the sport type spinner
        sportTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String content = parent.getItemAtPosition(position).toString();
                String content = MySportArray[position];
                sportName = content;
//                Utils.showToast(Tracking.this, "The selected sport is: " + content);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Daeun
        sharedPref = getSharedPreferences("healthCoin", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_sport_queryAll:
                Log.d("divider", "############################");
                try {
                    int recordsNr = 0;
                    List<SportEntity> list = sportDao.queryAll();
                    for (SportEntity item : list) {
                        recordsNr++;
                        Log.d("query_all_tag", item.toString());
                    }
                    Utils.showToast(this, "recordsNr = " + recordsNr);
                }catch (Exception e) {
                    Utils.showToast(this, e.getMessage());
                }
                break;

            case R.id.btn_reset_firstopen:
                SharedPreferencesUtil.getInstance(this).writeBoolean("first", true);
                Utils.showToast(this, "reset first open to true");
                break;

            case R.id.btnSportStart:
                if (!isRunning) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    /*
                     * setBase(): set from what time to start to count
                     * SystemClock.elapsedRealtime() = real time right now
                     */
                    chronometer.start();    //start the chronometer
                    isRunning = true;       //update the status boolean (on)
                }
                break;

            case R.id.btnSportStop:
                if (isRunning){
                    timeRecorded = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;   //the recorded time
                    timeRecordedMinute = timeRecorded / 60;
                    chronometer.stop();
                    isRunning = false;
                    chronometer.setBase(SystemClock.elapsedRealtime()); //set clock to 0
                    Toast toast = Toast.makeText(Tracking.this, getString(R.string.the_recorded_time_minute_is) + timeRecordedMinute, Toast.LENGTH_SHORT);
                    toast.show();
                    showDialogSeekBar();
                }
                break;

        }
    }

    //sport result dialog
    private void showDialogRecordResult(){
        AlertDialog.Builder dialogResult = new AlertDialog.Builder(this);
        dialogResult.setTitle(R.string.the_recorded_sport);
        dialogResult.setMessage(getString(R.string.sport_type) + sportName + getString(R.string.duration_minute) + String.format("%.2f", timeRecordedMinute) + getString(R.string.mood_score) + myMoodScore);
        dialogResult.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //todo here? you can jump to archive
            }
        });
        dialogResult.show();
    }


    private void showDialogSeekBar() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_seekbar, null);
        // mySeekBar
        SeekBar mySeekBar = mView.findViewById(R.id.myMoodScoreSeekBar);
        mySeekBar.setOnSeekBarChangeListener(this);
        mBuilder.setTitle(R.string.after_sport_seekbar);
        //put the seekbar into the dialog
        mBuilder.setView(mView);

        mBuilder.setPositiveButton(R.string.btn_after_sport_dialog_positive, (dialog, which) -> {
            // save to datebase;
            saveSportDataToDB();

            //show result dialog
            showDialogRecordResult();
            // todo? start questionaire?

            // Daeun
            showDialogReward();
        });

        mBuilder.setNegativeButton(R.string.btn_after_sport_dialog_negative, (dialog, which) -> {
           // back ot main page
            Intent intent = new Intent(Tracking.this, MainActivity.class);
            startActivity(intent);
        });


        //show the AlertDialog using show() method
        AlertDialog alertDialog2 = mBuilder.create();
        alertDialog2.show();
    }


    /*here is for mySeekBar.setOnSeekBarChangeListener(this)*/

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        myMoodScore = seekBar.getProgress();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }


    private void saveSportDataToDB() {
        // create SportEntity
        SportEntity sportEntity = new SportEntity();
        sportEntity.setTimeAndDateStamp(Utils.getCurrentDateAndTime());
        sportEntity.setName(sportName);
        sportEntity.setDuration(String.format("%.2f", timeRecordedMinute));
        sportEntity.setMoodScore(myMoodScore);

        sportDao.insert(sportEntity);
        Utils.showToast(Tracking.this, getString(R.string.sport_saved));
    }

    // Daeun
    private void showDialogReward(){
        float totalTime = 0;
        for(String duration : sportDao.queryAllDuration(sportName)){
            float sportTime = Float.parseFloat(duration.replace(',','.'));
            totalTime += sportTime;
        }

        goal = sharedPref.getInt(sportName, 0);
        if(totalTime>goal){
            healthCoin = sharedPref.getInt("healthCoin", 0);
            healthCoin++;
            editor.putInt("healthCoin", healthCoin);
            editor.apply();

            AlertDialog.Builder reward = new AlertDialog.Builder(this);
            reward.setTitle("Your health coins");
            reward.setMessage("Congratulations!\nYou did " + sportName + " for " + goal + " minutes.\nYour health coins: " + healthCoin);
            reward.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            reward.show();
        }

        ((MainActivity)MainActivity.context).updateHealthCoin();
        editor.putFloat(sportName + "_time", totalTime);
        editor.apply();
        ((MainActivity)MainActivity.context).updateSportTotalTime();
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        if (parent.getId() == R.id.sensorSpinner) {
            if (selectedItem.equals("Choose Sensor")) {

            } else if (selectedItem.equals("SensorData")) {
                openActivity(Sensors.class);
            } else if (selectedItem.equals("GpsData")) {
                openActivity(GPS.class);
            } else {
                // do nothing
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}