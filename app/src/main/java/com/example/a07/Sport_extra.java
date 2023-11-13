/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 08:04
 *
 */

package com.example.a07;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Sport_extra extends AppCompatActivity {

    Resources res;
    Spinner activity_spinner;

    Button datePickerbtn;
    TextView datePickertxt;
    DatePickerDialog datePickerDialog;

    TimePicker time_picker1;
    TimePicker time_picker2;
    TextView time_picker_txt1;
    TextView time_picker_txt2;

    SeekBar moodSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_extra);

        res = getResources();
        String[] activities = res.getStringArray(R.array.sportTypeArray);

        activity_spinner = findViewById(R.id.activity_spinner);

        ArrayAdapter<CharSequence> activity_adapter = ArrayAdapter.createFromResource(this, R.array.sportTypeArray, android.R.layout.simple_spinner_dropdown_item);
        activity_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity_spinner.setAdapter(activity_adapter);
        activity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Sport_extra.this, activities[position] + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        datePickertxt = findViewById(R.id.date_picker_text);
        datePickerbtn = findViewById(R.id.date_picker_btn);

        datePickerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int pYear = calendar.get(Calendar.YEAR);
                int pMonth = calendar.get(Calendar.MONTH);
                int pDay = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(Sport_extra.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month+1;
                                String date = year + "/" + month + "/" + dayOfMonth;
                                datePickertxt.setText(date);
                            }
                        }, pYear, pMonth, pDay);
                datePickerDialog.show();
            }
        });

        time_picker1 = findViewById(R.id.time_picker1);
        time_picker2 = findViewById(R.id.time_picker2);
        time_picker_txt1 = findViewById(R.id.time_picker_view1);
        time_picker_txt2 = findViewById(R.id.time_picker_view2);

        time_picker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                time_picker_txt1.setText(hourOfDay + ":" + minute + " selected");
            }
        });

        time_picker2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                time_picker_txt2.setText(hourOfDay + ":" + minute + " selected");
            }
        });

        moodSeekBar = findViewById(R.id.moodSeekbar);
        moodSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
