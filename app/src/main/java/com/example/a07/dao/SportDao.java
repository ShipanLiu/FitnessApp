/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 08:04
 *
 */

package com.example.a07.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.a07.entity.SportEntity;

import java.util.List;

@Dao
public interface SportDao {

    @Insert
    void insert(SportEntity... sportEntity);


    // query All Questionaires(Default:   table name = entity name)
    @Query("SELECT * FROM SportEntity")
    List<SportEntity> queryAll();

    @Query("SELECT duration FROM SportEntity WHERE name = :sport_name")
    List<String> queryAllDuration(String sport_name);


    // clear the "QuestionaireEntity" table
    @Query("DELETE FROM SportEntity")
    void deleteAll();

    @Delete
    void deleteItem(SportEntity... item);
}
