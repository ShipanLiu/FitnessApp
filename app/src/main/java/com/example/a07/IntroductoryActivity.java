/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 08:04
 *
 */

package com.example.a07;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;

public class IntroductoryActivity extends AppCompatActivity {

    LottieAnimationView animationView;
    @SuppressLint("StaticFieldLeak")
    public static Activity SplashActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_introductory);
        SplashActivity = IntroductoryActivity.this;

        animationView = findViewById(R.id.animationView);

        animationView.animate().translationX(2000).setDuration(2000).setStartDelay(2900);

        // Check if app was opened from notification
        if (getIntent().hasExtra("runFunction")) {
            String functionName = getIntent().getStringExtra("runFunction");
            // if app was opened from notification, open the questionnaire
            if (functionName.equals("openQuestionnaire")) {
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(IntroductoryActivity.this, Questionnaire.class);
                    startActivity(intent);
                },3900);
            }
        }
        // if app was opened normally, open the main activity
        else {
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(IntroductoryActivity.this, MainActivity.class);
                startActivity(intent);
            },3900);
        }
    }
}