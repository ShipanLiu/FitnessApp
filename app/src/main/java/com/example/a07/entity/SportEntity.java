/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 15.05.23, 16:32
 *
 */

package com.example.a07.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.a07.dao.SportDao;

import java.util.ArrayList;

@Entity
public class SportEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String timeAndDateStamp;

    private String name;

    private String duration;

    private int moodScore;


    public SportEntity(int id, String timeAndDateStamp, String name, String duration, int moodScore) {
        this.id = id;
        this.timeAndDateStamp = timeAndDateStamp;
        this.name = name;
        this.duration = duration;
        this.moodScore = moodScore;
    }

    public SportEntity() {
    }

    private static String[] timeAndDateStampArr = {
            "23:02:1998 12:00:01",
            "24:03:1999 18:56:49",
    };

    private static String[] nameArr = {
            "running",
            "swimming",
    };
    private static String[] durationArr = {
            "13",
            "35",
    };
    private static int[] moodScoreArr = {
            20,
            80
    };

    public static ArrayList<SportEntity> getDefaultList() {
        ArrayList<SportEntity> list = new ArrayList<>();
        for(int i = 0; i < nameArr.length; i++) {
            SportEntity sportEntity = new SportEntity();
            // if you use ROOM, you don't have to set up the id. not like DBHelper
//            sportEntity.setId(i);
            sportEntity.setTimeAndDateStamp(timeAndDateStampArr[i]);
            sportEntity.setName(nameArr[i]);
            sportEntity.setDuration(durationArr[i]);
            sportEntity.setMoodScore(moodScoreArr[i]);
            list.add(sportEntity);
        }
        return list;
    }

    public static String[] getTimeAndDateStampArr() {
        return timeAndDateStampArr;
    }

    public static String[] getNameArr() {
        return nameArr;
    }

    public static String[] getDurationArr() {
        return durationArr;
    }

    public static int[] getMoodScoreArr() {
        return moodScoreArr;
    }

    public int getId() {
        return id;
    }

    public String getTimeAndDateStamp() {
        return timeAndDateStamp;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public int getMoodScore() {
        return moodScore;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimeAndDateStamp(String timeAndDateStamp) {
        this.timeAndDateStamp = timeAndDateStamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setMoodScore(int moodScore) {
        this.moodScore = moodScore;
    }

    @Override
    public String toString() {
        return "Sport{" +
                "id=" + id +
                ", timeAndDateStamp='" + timeAndDateStamp + '\'' +
                ", name='" + name + '\'' +
                ", duration='" + duration + '\'' +
                ", moodScore=" + moodScore +
                '}';
    }
}
