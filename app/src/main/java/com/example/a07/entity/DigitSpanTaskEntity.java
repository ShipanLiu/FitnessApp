/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 12:12
 *
 */

package com.example.a07.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName= "digitSpanTable")
public class DigitSpanTaskEntity {

    public DigitSpanTaskEntity() {
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String timeAndDateStamp;

    private int sequenceLength;

    private long responseTime;



    public DigitSpanTaskEntity(String timeAndDateStamp, int sequenceLength, long responseTime){
        this.timeAndDateStamp= timeAndDateStamp;
        this.sequenceLength= sequenceLength;
        this.responseTime= responseTime;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeAndDateStamp() {
        return timeAndDateStamp;
    }

    public int getSequenceLength() {
        return sequenceLength;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setTimeAndDateStamp(String timeAndDateStamp) {
        this.timeAndDateStamp = timeAndDateStamp;
    }

    public void setSequenceLength(int sequenceLength) {
        this.sequenceLength = sequenceLength;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }
}
