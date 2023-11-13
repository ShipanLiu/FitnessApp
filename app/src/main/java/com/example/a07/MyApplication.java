/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 12:12
 *
 */

package com.example.a07;

import android.app.Application;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;

import com.example.a07.dao.QuesDao;
import com.example.a07.dao.SportDao;
import com.example.a07.database.AppDatabase;
import com.example.a07.database.DigitSpanTaskDatabase;
import com.example.a07.database.GpsDatabase;
import com.example.a07.database.SensorDatabase;
import com.example.a07.database.SportDatabase;
import com.example.a07.entity.SportEntity;
import com.example.a07.utils.SharedPreferencesUtil;
import com.example.a07.utils.Utils;

import java.util.HashMap;
import java.util.List;

public class MyApplication extends Application {

    // record the total sport time
    public int sportTotalTime;

    // recode if one item is clicked or not in the Questionaire
    public HashMap<Integer, Boolean> clickMap = new HashMap<>();


    private static MyApplication mApp;

    // create AppDatabase immediately after starting the APP
    private AppDatabase appDatabase;
    private SportDatabase sportDatabase;


    private SensorDatabase sensorDatabase;
    private GpsDatabase gpsDatabase;
    private DigitSpanTaskDatabase digitSpanTaskDatabase;


    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        // the name of database is ""
        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "appdb")
                .addMigrations()
                .allowMainThreadQueries()
                .build();

        sportDatabase = Room.databaseBuilder(this, SportDatabase.class, "sportdb")
                .addMigrations()
                .allowMainThreadQueries()
                .build();
        sensorDatabase = Room.databaseBuilder(this, SensorDatabase.class, "sensordb")
                .addMigrations()
                .allowMainThreadQueries()
                .build();
        gpsDatabase = Room.databaseBuilder(this, GpsDatabase.class, "gpsdb")
                .addMigrations()
                .allowMainThreadQueries()
                .build();
        digitSpanTaskDatabase = Room.databaseBuilder(this ,DigitSpanTaskDatabase.class, "digitspantaskdb")
                .addMigrations()
                .allowMainThreadQueries()
                .build();


        // insert some mock data;
        insertMockSportData();

        // initialize the click list to indicate which question is modified
        for(int i = 1; i <= 21; i++) {
            clickMap.put(i, false);
        }

    }

    public static MyApplication getInstance() {
        return mApp;
    }

    public AppDatabase getAppDatebase() {
        return appDatabase;
    }

    public SportDatabase getSportDatabase() {
        return sportDatabase;
    }

    public SensorDatabase getSensorDatabase() {
        return sensorDatabase;
    }

    public GpsDatabase getGpsDatabase() {
        return gpsDatabase;
    }
    public DigitSpanTaskDatabase getDigitSpanTaskDatabase() {
        return digitSpanTaskDatabase;
    }

    // at the first start of the app, 2 mock values is always inserted into the database
    private void insertMockSportData() {
        // if this is the first time open the phone, insert the mock data into database
        boolean isFirst = SharedPreferencesUtil.getInstance(this).readBoolean("first", true);
        if(isFirst) {
            Utils.showToast(this, getString(R.string.welcome_to_fit_mood));
            // first get SportDao:
            SportDao mySportDao = sportDatabase.sportDao();
            // write 2 mock data into database
            List<SportEntity> list = SportEntity.getDefaultList();
            for(SportEntity item : list) {
                mySportDao.insert(item);
            }

            // set first open app to false
            SharedPreferencesUtil.getInstance(this).writeBoolean("first", false);
        }
    }
}
