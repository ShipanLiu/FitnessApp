/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 13.06.23, 16:16
 *
 */

package com.example.a07.dao;

import androidx.room.Dao;

import androidx.room.Insert;
import androidx.room.Query;



import com.example.a07.entity.SensorEntity;


import java.util.List;

@Dao
public interface SensorDao {
    @Insert
    void insert(SensorEntity...  sensorEntity);

    @Query("SELECT * FROM sensorentity")
    List<SensorEntity> queryAll();


    // clear the " table
    @Query("DELETE FROM sensorentity")
    void deleteAll();



}
