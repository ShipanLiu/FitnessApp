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


import com.example.a07.entity.GpsEntity;


import java.util.List;

@Dao
public interface GpsDao {
    @Insert
    void insert(GpsEntity gps);

    @Query("SELECT * FROM gps_table")
    List<GpsEntity> queryAll();


    // clear the " table
    @Query("DELETE FROM gps_table")
    void deleteAll();



}
