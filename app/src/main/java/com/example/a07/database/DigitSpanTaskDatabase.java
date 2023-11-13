/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 12:12
 *
 */

package com.example.a07.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;


import com.example.a07.dao.DigitSpanTaskDao;

import com.example.a07.entity.DigitSpanTaskEntity;


@Database(entities= {DigitSpanTaskEntity.class}, version = 1, exportSchema = true)
public abstract class DigitSpanTaskDatabase extends RoomDatabase{

    //exposure DigitSpanTaskDao
    public abstract DigitSpanTaskDao digitSpanTaskDao();
}
