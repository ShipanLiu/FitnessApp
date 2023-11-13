/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 08:04
 *
 */

package com.example.a07.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.a07.entity.QuestionaireEntity;
import com.example.a07.entity.SportEntity;

import java.util.List;

@Dao
public interface QuesDao {

    @Insert
    void insert(QuestionaireEntity... quesEntity);

    // query All Questionaires(Default:   table name = entity name)
    @Query("SELECT * FROM QuestionaireEntity")
    List<QuestionaireEntity> queryAll();


    // clear the "QuestionaireEntity" table
    @Query("DELETE FROM QuestionaireEntity")
    void deleteAll();

    @Query("SELECT id FROM QuestionaireEntity ORDER BY id DESC LIMIT 1")
    Integer getLastQuestionnaire();

    @Query("UPDATE QuestionaireEntity SET ex_trigger = :newValue1, ex_materialNr = :newValue2, ex_rating = :newValue3, ex_comment = :newValue4 WHERE id = :questionnaireId")
    void updateQuestionnaire(int questionnaireId, Boolean newValue1, int newValue2, int newValue3, String newValue4);

}
