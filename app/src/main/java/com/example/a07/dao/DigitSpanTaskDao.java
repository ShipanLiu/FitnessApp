/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 12:12
 *
 */

package com.example.a07.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.a07.entity.DigitSpanTaskEntity;

@Dao
public interface DigitSpanTaskDao {

    @Insert
    void insert(DigitSpanTaskEntity digitSpanTask);
    @Query("DELETE FROM digitSpanTable")
    void deletAll();
}
