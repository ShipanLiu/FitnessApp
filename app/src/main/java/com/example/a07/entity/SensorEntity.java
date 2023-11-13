/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 13.06.23, 16:16
 *
 */

package com.example.a07.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SensorEntity")
public class SensorEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String timeAndDateStamp;

    private Integer stepCount;

    public SensorEntity() {
    }


    public SensorEntity(int id, String timeAndDateStamp, Integer stepCount) {
        this.id = id;
        this.timeAndDateStamp = timeAndDateStamp;
        this.stepCount = stepCount;


    }

    public int getId() {
        return id;
    }

    public String getTimeAndDateStamp() {
        return timeAndDateStamp;
    }

    public Integer getStepCount() {
        return stepCount;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setTimeAndDateStamp(String timeAndDateStamp) {
        this.timeAndDateStamp = timeAndDateStamp;
    }

    public void setStepCount(Integer stepCount) {
        this.stepCount = stepCount;
    }




}
