/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 08:04
 *
 */

package com.example.a07.utils;

import com.example.a07.entity.QuestionaireEntity;
import java.util.Random;

public class TriggerAlgo01 implements TriggerAlgo{
    //the 1st algorithm to caulculate the user wellbeing
    @Override
    public Boolean algo(QuestionaireEntity entity) {
        Boolean result = false;
        //to caulculate Mood/Stress level
        float ques1 = entity.getQues1();
        float ques3 = entity.getQues3();
        float quesAverage = (ques1 + ques3) / 2;
        if (quesAverage < 50){                  //if the quesAverage is lower than 50:
            Random r = new Random();
            result = r.nextBoolean();     //50% possibility to show a video from the pool
        }
        return result;
    }
}
