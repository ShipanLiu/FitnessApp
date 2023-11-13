

/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 25.07.23, 22:25
 *
 */

package com.example.a07;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a07.adapter.SportArchivAdapter;
import com.example.a07.dao.SportDao;
import com.example.a07.entity.SportEntity;
import com.example.a07.utils.Utils;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class Archive extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    private List<SportEntity> sportList;
    private ListView listViewSport;
    private SportArchivAdapter sportArchivAdapter;
    private SportDao sportDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        listViewSport = findViewById(R.id.listView_sport);
        sportDao = MyApplication.getInstance().getSportDatabase().sportDao();

        LineChart lineChart = findViewById(R.id.data_chart);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMaximum(100f); // This will make the left y-axis scale from 0 to 100
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setAxisMaximum(180f); // This will make the right y-axis scale from 0 to 360
        rightAxis.setAxisMinimum(0f);

        sportList = sportDao.queryAll();
        LineDataSet moodScoreDataSet = getMoodScoreDataSet(sportList);
        LineDataSet durationDataSet = getDurationDataSet(sportList);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(moodScoreDataSet);
        dataSets.add(durationDataSet);
        LineData data = new LineData(dataSets);

        lineChart.setData(data);
        lineChart.invalidate(); // refresh
    }

    private LineDataSet getMoodScoreDataSet(List<SportEntity> sportEntities) {
        ArrayList<Entry> valueSet = new ArrayList<>();

        for (int i = 0; i < sportEntities.size(); i++) {
            SportEntity sportEntity = sportEntities.get(i);
            valueSet.add(new Entry(i, (float)sportEntity.getMoodScore()));
        }

        LineDataSet lineDataSet = new LineDataSet(valueSet, "Mood Score Data");
        lineDataSet.setColor(Color.rgb(255, 0, 255));

        return lineDataSet;
    }

    private LineDataSet getDurationDataSet(List<SportEntity> sportEntities) {
        ArrayList<Entry> valueSet = new ArrayList<>();
        float minDuration = 0f;
        float maxDuration = 180f;  // assuming max duration can be 6 hours (360 minutes)

        for (int i = 0; i < sportEntities.size(); i++) {
            SportEntity sportEntity = sportEntities.get(i);
            float normalizedDuration = normalizeDuration(sportEntity.getDuration(), minDuration, maxDuration);
            valueSet.add(new Entry(i, normalizedDuration));
        }

        LineDataSet lineDataSet = new LineDataSet(valueSet, "Duration Data");
        lineDataSet.setColor(Color.rgb(0, 255, 255));
        lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        return lineDataSet;
    }

    private float normalizeDuration(String duration, float minDuration, float maxDuration) {
        String[] parts = duration.split("\\."); // Split the string at the "."
        float minutes = Float.parseFloat(parts[0]);
        float seconds = parts.length > 1 ? Float.parseFloat(parts[1]) : 0;
        float totalMinutes = minutes + (seconds / 60f); // Convert duration to total minutes

        return ((totalMinutes - minDuration) / (maxDuration - minDuration)) * 100;
    }


    // show list on resume
    @Override
    protected void onResume() {
        super.onResume();
        showSportList();
    }

    private void showSportList() {
        // get sport List from database
        sportList = sportDao.queryAll();
        if(sportList.size() == 0) {
            return;
        }

        // now you get the datalist, so it's time to get the adapter
        sportArchivAdapter = new SportArchivAdapter(this, sportList);
        // ListView receive the adapter
        listViewSport.setAdapter(sportArchivAdapter);

        // add click long listener,  delete item
        listViewSport.setOnItemLongClickListener(this);
    }

    // long click on a item to delete it
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

        // target item
        SportEntity item =  sportList.get(position);
        // dialog
        AlertDialog.Builder builder =  new AlertDialog.Builder(Archive.this);
        builder.setMessage(getString(R.string.sport_dialog_delete) + " " + item.getName() + " at " + item.getTimeAndDateStamp() + "?");
        builder.setPositiveButton(R.string.sport_dialog_yes, (dialog, which) -> {
            // delete from database
            sportDao.deleteItem(item);
            // delete this info in list
            sportList.remove(position);
            // refresh the adapter
            sportArchivAdapter.notifyDataSetChanged();

            Utils.showToast(this,  R.string.sport_dialog_delete + " " +  item.getName());
        });

        builder.setNegativeButton(R.string.sport_dialog_no, null);
        builder.create().show();
        return true;
    }

    @Override
    public void onClick(View view) {

    }

    // NAVIGATION
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