/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 15.05.23, 16:32
 *
 */

package com.example.a07.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.a07.dao.SportDao;
import com.example.a07.entity.SportEntity;

@Database(entities = {SportEntity.class}, version = 1, exportSchema = true)
public abstract class SportDatabase extends RoomDatabase {

    public abstract SportDao sportDao();
}
