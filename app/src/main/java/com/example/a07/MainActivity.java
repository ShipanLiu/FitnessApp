/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 25.07.23, 15:06
 *
 */

package com.example.a07;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    IntroductoryActivity SA = (IntroductoryActivity) IntroductoryActivity.SplashActivity;

    private final static String[] MySportArray = {"Wandering", "Jogging", "Swimming", "Ball sports", "Weight training", "Yoga", "Climbing/Bouldering","Others" };
    // Daeun: for showing the amount of user's health coin
    private TextView healthCoinAmount;
    private int healthCoin = 0;
    public static Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    // Daeun: for setting personalized goal
    private Spinner sportSpinner;
    private EditText goalSetting;
    private Button goalSetting_btn;
    private int selectedTime = 0;

    // Daeun: monitoring the progress

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SA.finish();
        setContentView(R.layout.activity_main);

        // Check if the user has already given consent
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("my_app_preferences", MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("hasUserGivenConsent", false)) {
            Intent intent = new Intent(this, ConsentActivity.class);
            startActivity(intent);
        }

        sharedPref = getSharedPreferences("healthCoin", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        context = this;
        updateHealthCoin();

        sportSpinner = findViewById(R.id.sportSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sportTypeArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);         // Specify the layout to use when the list of choices appears
        sportSpinner.setAdapter(adapter);                                                    // Apply the adapter to the spinner
        sportSpinner.setSelection(0);
        sportSpinner.setOnItemSelectedListener(this);

        goalSetting = findViewById(R.id.goal);
        goalSetting_btn = findViewById(R.id.goal_setting_btn);
        goalSetting_btn.setOnClickListener(this);

        updateSportTotalTime();
    }



    public void updateHealthCoin(){
        healthCoin = sharedPref.getInt("healthCoin", 0);
        TextView healthCoinAmount_txt = findViewById(R.id.health_coin_amount);
        healthCoinAmount_txt.setText(String.valueOf(healthCoin));
    };

    public void updateSportTotalTime(){
        ((TextView) findViewById(R.id.wandering_total_time)).setText(String.valueOf(sharedPref.getFloat("Wandering_time", 0.00f)) + " min");
        ((TextView) findViewById(R.id.jogging_total_time)).setText(String.valueOf(sharedPref.getFloat("Jogging_time", 0.00f)) + " min");
        ((TextView) findViewById(R.id.swimming_total_time)).setText(String.valueOf(sharedPref.getFloat("Swimming_time", 0.00f)) + " min");
        ((TextView) findViewById(R.id.ball_sports_total_time)).setText(String.valueOf(sharedPref.getFloat("Ball sports_time", 0.00f)) + " min");
        ((TextView) findViewById(R.id.weight_training_total_time)).setText(String.valueOf(sharedPref.getFloat("Weight training_time", 0.00f)) + " min");
        ((TextView) findViewById(R.id.yoga_total_time)).setText(String.valueOf(sharedPref.getFloat("Yoga_time", 0.00f)) + " min");
        ((TextView) findViewById(R.id.climbing_bouldering_total_time)).setText(String.valueOf(sharedPref.getFloat("Climbing/bouldering_time", 0.00f)) + " min");
        ((TextView) findViewById(R.id.others_total_time)).setText(String.valueOf(sharedPref.getFloat("Ohters_time", 0.00f)) + " min");

    };

    private long backKeyPressedTime = 0;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast toast = Toast.makeText(this, R.string.press_back_again_to_exit, Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
        }
    }

    // Method to switch to a different activity based on the tag attribute of the clicked view
    public void switchActivity(View view) {
        String tag = view.getTag().toString();  // get the tag attribute of the clicked view as a string
        try {
            Class<?> activityClass = Class.forName(getPackageName() + "." + tag);       // construct the class name of the activity to be opened
            openActivity(activityClass);        // call openActivity with class of the activity to be opened
        } catch (ClassNotFoundException e) {
            e.printStackTrace();                // handle exceptions
        }
    }

    // Method to open an activity based on its class
    public void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);    // create an intent to open the activity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);                // set the flag to clear the activity stack
        startActivity(intent);                                          // start the activity
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.goal_setting_btn){
//            String selectedSport = sportSpinner.getSelectedItem().toString();
            String selectedSport = MySportArray[sportSpinner.getSelectedItemPosition()];

            selectedTime = Integer.parseInt(String.valueOf(goalSetting.getText()));
            editor.putInt(selectedSport, selectedTime);
            editor.apply();

            showDialogSettingGoal();
        }
    }

    private void showDialogSettingGoal(){
        AlertDialog.Builder user_goal = new AlertDialog.Builder(this);
        user_goal.setTitle(getString(R.string.daeun_dialog_yourgoal));
        user_goal.setMessage(getString(R.string.wandering) + ": " + sharedPref.getInt("Wandering", 0) +  getString(R.string.daeun_dialog_mins)+ "\n" +
                getString(R.string.jogging) + ": "  + sharedPref.getInt("Jogging", 0) + getString(R.string.daeun_dialog_mins)+ "\n" +
                getString(R.string.swimming) + ": "  + sharedPref.getInt("Swimming", 0) + getString(R.string.daeun_dialog_mins)+ "\n" +
                getString(R.string.ball_sports) + ": "  + sharedPref.getInt("Ball sports", 0) + getString(R.string.daeun_dialog_mins)+ "\n" +
                getString(R.string.weight_training) + ": "  + sharedPref.getInt("Weight training", 0) + getString(R.string.daeun_dialog_mins)+ "\n" +
                getString(R.string.yoga) + ": "  + sharedPref.getInt("Yoga", 0) + getString(R.string.daeun_dialog_mins) + "\n" +
                getString(R.string.climbing_bouldering) + ": "  + sharedPref.getInt("Climbing/Bouldering", 0) + getString(R.string.daeun_dialog_mins) + "\n" +
                getString(R.string.ohters) + ": "  + sharedPref.getInt("Others", 0) + getString(R.string.daeun_dialog_mins));

//        user_goal.setMessage("Wandering: " + sharedPref.getInt("Wandering", 0) + " min\n" +
//                "Jogging: " + sharedPref.getInt("Jogging", 0) + " min\n" +
//                "Ball sports: " + sharedPref.getInt("Ball sports", 0) + " min\n" +
//                "Weight training: " + sharedPref.getInt("Weight training", 0) + " min\n" +
//                "Yoga: " + sharedPref.getInt("Yoga", 0) + " min\n" +
//                "Climbing/Bouldering: " + sharedPref.getInt("Climbing/Bouldering", 0) + " min\n" +
//                "Others: " + sharedPref.getInt("Others", 0) + " min");

        user_goal.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        user_goal.show();
    }

}