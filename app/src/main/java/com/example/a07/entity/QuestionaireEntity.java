/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 08:04
 *
 */

package com.example.a07.entity;


// this is an Entity type for the SQL

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class QuestionaireEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
//    private String quesUuid;
    private String timeAndDateStamp;
    private float ques1;
    private float ques2;
    private float ques3;
    private float ques4;
    private float ques5;
    private float ques6;

    private float ques7;
    private float ques8;

    private String ques9;
    private float ques10;

    private String ques11;

    private String ques12;

    private float ques13;
    private float ques14;
    private float ques15;
    private float ques16;

    private float ques17;
    private float ques18;

    private float ques19;
    private float ques20;
    private String ques21;
    private Boolean ex_trigger;
    private Integer ex_materialNr;
    private Integer ex_rating;
    private String ex_comment;

    public QuestionaireEntity() {
        this.id = id;
        this.timeAndDateStamp = timeAndDateStamp;
        this.ques1 = ques1;
        this.ques2 = ques2;
        this.ques3 = ques3;
        this.ques4 = ques4;
        this.ques5 = ques5;
        this.ques6 = ques6;
        this.ques7 = ques7;
        this.ques8 = ques8;
        this.ques9 = ques9;
        this.ques10 = ques10;
        this.ques11 = ques11;
        this.ques12 = ques12;
        this.ques13 = ques13;
        this.ques14 = ques14;
        this.ques15 = ques15;
        this.ques16 = ques16;
        this.ques17 = ques17;
        this.ques18 = ques18;
        this.ques19 = ques19;
        this.ques20 = ques20;
        this.ques21 = ques21;
        // exercise triggered
        this.ex_trigger = ex_trigger;
        // which video
        this.ex_materialNr = ex_materialNr;
        // exercise rating
        this.ex_rating = ex_rating;
        // comment the exercise
        this.ex_comment = ex_comment;
    }

    public int getId() {
        return id;
    }

    public String getTimeAndDateStamp() {
        return timeAndDateStamp;
    }

    public float getQues1() {
        return ques1;
    }

    public float getQues2() {
        return ques2;
    }

    public float getQues3() {
        return ques3;
    }

    public float getQues4() {
        return ques4;
    }

    public float getQues5() {
        return ques5;
    }

    public float getQues6() {
        return ques6;
    }

    public float getQues7() {
        return ques7;
    }

    public float getQues8() {
        return ques8;
    }

    public String getQues9() {
        return ques9;
    }

    public float getQues10() {
        return ques10;
    }

    public String getQues11() {
        return ques11;
    }

    public String getQues12() {
        return ques12;
    }

    public float getQues13() {
        return ques13;
    }

    public float getQues14() {
        return ques14;
    }

    public float getQues15() {
        return ques15;
    }

    public float getQues16() {
        return ques16;
    }

    public float getQues17() {
        return ques17;
    }

    public float getQues18() {
        return ques18;
    }

    public float getQues19() {
        return ques19;
    }

    public float getQues20() {
        return ques20;
    }

    public String getQues21() {
        return ques21;
    }

    public Boolean getEx_trigger() {
        return ex_trigger;
    }

    public Integer getEx_materialNr() {
        return ex_materialNr;
    }

    public Integer getEx_rating() {
        return ex_rating;
    }

    public String getEx_comment() {
        return ex_comment;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimeAndDateStamp(String timeAndDateStamp) {
        this.timeAndDateStamp = timeAndDateStamp;
    }

    public void setQues1(float ques1) {
        this.ques1 = ques1;
    }

    public void setQues2(float ques2) {
        this.ques2 = ques2;
    }

    public void setQues3(float ques3) {
        this.ques3 = ques3;
    }

    public void setQues4(float ques4) {
        this.ques4 = ques4;
    }

    public void setQues5(float ques5) {
        this.ques5 = ques5;
    }

    public void setQues6(float ques6) {
        this.ques6 = ques6;
    }

    public void setQues7(float ques7) {
        this.ques7 = ques7;
    }

    public void setQues8(float ques8) {
        this.ques8 = ques8;
    }

    public void setQues9(String ques9) {
        this.ques9 = ques9;
    }

    public void setQues10(float ques10) {
        this.ques10 = ques10;
    }

    public void setQues11(String ques11) {
        this.ques11 = ques11;
    }

    public void setQues12(String ques12) {
        this.ques12 = ques12;
    }

    public void setQues13(float ques13) {
        this.ques13 = ques13;
    }

    public void setQues14(float ques14) {
        this.ques14 = ques14;
    }

    public void setQues15(float ques15) {
        this.ques15 = ques15;
    }

    public void setQues16(float ques16) {
        this.ques16 = ques16;
    }

    public void setQues17(float ques17) {
        this.ques17 = ques17;
    }

    public void setQues18(float ques18) {
        this.ques18 = ques18;
    }

    public void setQues19(float ques19) {
        this.ques19 = ques19;
    }

    public void setQues20(float ques20) {
        this.ques20 = ques20;
    }

    public void setQues21(String ques21) {
        this.ques21 = ques21;
    }

    public void setEx_trigger(Boolean ex_trigger) {
        this.ex_trigger = ex_trigger;
    }

    public void setEx_materialNr(Integer ex_materialNr) {
        this.ex_materialNr = ex_materialNr;
    }

    public void setEx_rating(Integer ex_rating) {
        this.ex_rating = ex_rating;
    }

    public void setEx_comment(String ex_comment) {
        this.ex_comment = ex_comment;
    }

    @Override
    public String toString() {
        return "QuestionaireEntity{" +
                "id=" + id +
                ", timeAndDateStamp='" + timeAndDateStamp + '\'' +
                ", ques1=" + ques1 +
                ", ques2=" + ques2 +
                ", ques3=" + ques3 +
                ", ques4=" + ques4 +
                ", ques5=" + ques5 +
                ", ques6=" + ques6 +
                ", ques7=" + ques7 +
                ", ques8=" + ques8 +
                ", ques9='" + ques9 + '\'' +
                ", ques10=" + ques10 +
                ", ques11='" + ques11 + '\'' +
                ", ques12='" + ques12 + '\'' +
                ", ques13=" + ques13 +
                ", ques14=" + ques14 +
                ", ques15=" + ques15 +
                ", ques16=" + ques16 +
                ", ques17=" + ques17 +
                ", ques18=" + ques18 +
                ", ques19=" + ques19 +
                ", ques20=" + ques20 +
                ", ques21='" + ques21 + '\'' +
                ", ex_trigger=" + ex_trigger +
                ", ex_materialNr=" + ex_materialNr +
                ", ex_rating=" + ex_rating +
                ", ex_comment='" + ex_comment + '\'' +
                '}';
    }
}
