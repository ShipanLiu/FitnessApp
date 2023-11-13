/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 12:12
 *
 */

package com.example.a07;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a07.dao.DigitSpanTaskDao;

import com.example.a07.entity.DigitSpanTaskEntity;

import com.example.a07.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DigitSpanTask extends AppCompatActivity implements View.OnClickListener{

    private TextView sequenceTextView;
    private EditText userInputEditText;
    private Button submitButton;

    private List<Integer> digitSequence;
    private int sequenceLength;

    private int trialCount;

    private long startTime;
    private long endTime;
    private long responseTime;
    private DigitSpanTaskDao digitSpanTaskDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digit_span_task);
        digitSpanTaskDao = MyApplication.getInstance().getDigitSpanTaskDatabase().digitSpanTaskDao();

        sequenceTextView = findViewById(R.id.sequenceTextView);
        userInputEditText = findViewById(R.id.userInputEditText);
        submitButton = findViewById(R.id.submitButton);
        findViewById(R.id.clearTable).setOnClickListener(this);

        sequenceLength = 3; // Initial sequence length
        digitSequence = generateDigitSequence(sequenceLength);

        trialCount = 0;



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserInput();

            }
        });

        displaySequence();
    }

    private void displaySequence() {
        sequenceTextView.setText(digitSequence.toString().replaceAll("[\\[\\], ]", ""));
        startTime = System.currentTimeMillis();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sequenceTextView.setText("");
            }
        }, 5000); // Adjust the duration as needed (in milliseconds)
    }

    private void checkUserInput() {
        String userInput = userInputEditText.getText().toString().trim();

        endTime = System.currentTimeMillis();
        responseTime = endTime - startTime;

        if (userInput.isEmpty()) {
            Toast.makeText(this, "Please enter a digit sequence.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userInput.length() != digitSequence.size()) {
            Toast.makeText(this, "Incorrect sequence length. Try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isCorrect = true;

        for (int i = 0; i < digitSequence.size(); i++) {
            int expectedDigit = digitSequence.get(i);
            int enteredDigit = Character.getNumericValue(userInput.charAt(i));

            if (enteredDigit != expectedDigit) {
                isCorrect = false;
                break;
            }
        }



        if (isCorrect) {
            Toast.makeText(this, "Congratulations! You entered the correct sequence.", Toast.LENGTH_SHORT).show();
            handleSequenceSuccess(responseTime);

        } else {
            handleSequenceFailure();
        }
    }

    private void handleSequenceSuccess(long responseTime) {


        sequenceLength++;
        digitSequence = generateDigitSequence(sequenceLength);
        Toast.makeText(this, "Sequence length increased by 1.", Toast.LENGTH_SHORT).show();

        userInputEditText.setText("");


        Toast.makeText(this, "Response Time: " + responseTime + " ms", Toast.LENGTH_SHORT).show();
        displaySequence();
        saveLoctationToDB();
    }

    private void handleSequenceFailure() {
        trialCount++;

        if (trialCount < 2) {
            Toast.makeText(this, "Incorrect sequence. Try again. Trial: " + trialCount, Toast.LENGTH_SHORT).show();
            userInputEditText.setText("");
        } else {
            Toast.makeText(this, "Maximum trials reached. Starting a new sequence.", Toast.LENGTH_SHORT).show();


            digitSequence = generateDigitSequence(sequenceLength);

            trialCount = 0;
            userInputEditText.setText("");
            displaySequence();
        }
    }

    private List<Integer> generateDigitSequence(int length) {
        List<Integer> sequence = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            sequence.add(random.nextInt(10));
        }

        return sequence;
    }
    private void saveLoctationToDB() {
        // Create a new GpsEntity and set the latitude, longitude, and timestamp
        DigitSpanTaskEntity digitSpanTaskEntity = new DigitSpanTaskEntity();
        digitSpanTaskEntity.setSequenceLength(sequenceLength);
        digitSpanTaskEntity.setResponseTime(responseTime);
        digitSpanTaskEntity.setTimeAndDateStamp(Utils.getCurrentDateAndTime());

        // Insert the GpsEntity into the database using the gpsDao
        digitSpanTaskDao.insert(digitSpanTaskEntity);

        Utils.showToast(DigitSpanTask.this, " Data saved to database");
    }

    @Override
    public void onClick(View view) {
        if( view.getId()==R.id.clearTable){
        try {
            digitSpanTaskDao.deletAll();
            Utils.showToast(this, "table cleared");
        }catch (Exception e) {
            Utils.showToast(this, e.getMessage());
        }
      }

    }
}
