/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 08:13
 *
 */

package com.example.a07;

/*
* in this page, the date should be saved into database
*
* */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.a07.dao.QuesDao;
import com.example.a07.entity.QuestionaireEntity;
import com.example.a07.utils.TriggerAlgo;
import com.example.a07.utils.TriggerAlgo01;
import com.example.a07.utils.Utils;

import java.util.HashMap;
import java.util.List;

public class Questionaire_confirm extends AppCompatActivity implements View.OnClickListener {

    // clickMap which contains the click information of each questionaire item
    private HashMap<Integer, Boolean> myClickMap = new HashMap<>();

    // ques 21
    private EditText ques21_editText;

    // the Entity to be saved in DB
    QuestionaireEntity quesEntity = new QuestionaireEntity();

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private QuesDao quesDao;

    // the questionaire id(this id should be delivered into ExerciseVideoActivity)
    private int quesId = -1;


    // save button:
    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ques_confirm);

        // ques21
        ques21_editText = findViewById(R.id.ques21_editText);
        ques21_editText.addTextChangedListener(new MyTextWatcher(ques21_editText));

        // get preferences and create editor
        preferences = getSharedPreferences("ques_data", Context.MODE_PRIVATE);
        editor = preferences.edit();

        saveButton = findViewById(R.id.btn_save_ques);
        saveButton.setOnClickListener(this);
        findViewById(R.id.btn_query_all_ques).setOnClickListener(this);

        quesDao = MyApplication.getInstance().getAppDatebase().quesDao();

    }


    // as soon as the user enter this page, do onResume()
    @Override
    protected void onResume() {
        super.onResume();
        myClickMap = MyApplication.getInstance().clickMap;
    }

    private Integer checkAndSaveInt(SharedPreferences preferences, Integer quesNr, int defaultvalue) {
        // if the question item is clicked
        if(myClickMap.get(quesNr)) {
            return preferences.getInt("ques"+quesNr, defaultvalue);
        } else {
            return defaultvalue;
        }

    }

    private String checkAndSaveString(SharedPreferences preferences, Integer quesNr, String defaultvalue) {
        // if the question item is clicked
        if(myClickMap.get(quesNr)) {
            return preferences.getString("ques"+quesNr, defaultvalue);
        } else {
            return defaultvalue;
        }

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_save_ques:
//                System.out.println(myClickMap);

                quesEntity.setTimeAndDateStamp(Utils.getCurrentDateAndTime());

                // get data from SharedPreferences and put into instance
                // if you does not scroll the seekbar, the the default value will be saved into the database;
                quesEntity.setQues1(checkAndSaveInt(preferences, 1, 0));
                quesEntity.setQues2(checkAndSaveInt(preferences, 2, 0));
                quesEntity.setQues3(checkAndSaveInt(preferences, 3, 0));
                quesEntity.setQues4(checkAndSaveInt(preferences, 4, 0));
                quesEntity.setQues5(checkAndSaveInt(preferences, 5, 0));
                quesEntity.setQues6(checkAndSaveInt(preferences, 6, 0));

                quesEntity.setQues7(checkAndSaveInt(preferences, 7, 50));
                quesEntity.setQues8(checkAndSaveInt(preferences, 8, 50));


                quesEntity.setQues9(checkAndSaveString(preferences, 9, "alone"));
                quesEntity.setQues10(checkAndSaveInt(preferences, 10, 50));

                quesEntity.setQues11(checkAndSaveString(preferences, 11, "Partner"));

                quesEntity.setQues12(checkAndSaveString(preferences, 12, "At home"));

                quesEntity.setQues13(checkAndSaveInt(preferences, 13, 50));
                quesEntity.setQues14(checkAndSaveInt(preferences, 14, 50));
                quesEntity.setQues15(checkAndSaveInt(preferences, 15, 50));
                quesEntity.setQues16(checkAndSaveInt(preferences, 16, 50));

                quesEntity.setQues17(checkAndSaveInt(preferences, 17, 4));
                quesEntity.setQues18(checkAndSaveInt(preferences, 18, 4));

                quesEntity.setQues19(checkAndSaveInt(preferences, 19, 3));
                quesEntity.setQues20(checkAndSaveInt(preferences, 20, 3));
                quesEntity.setQues21(checkAndSaveString(preferences, 21, "the user does not have comment"));

                // set default values for the exercise evaluation
                quesEntity.setEx_trigger(false);
                quesEntity.setEx_materialNr(1);
                quesEntity.setEx_rating(50);
                quesEntity.setEx_comment("the user does not comment this exercise");

//              Log.d("test_tag", quesEntity.toString());

                // save into database
                try {
                    quesDao.insert(quesEntity);
                    Utils.showToast(this, getString(R.string.insert_with_success));
                }catch (Exception e) {
                    Utils.showToast(this, e.getMessage());
                }

                // set "save button" disable
                saveButton.setEnabled(false);

                //apply the algorithm
                //todo: make it easy to change the algorithm, not enough?
                TriggerAlgo triggerAlgo = new TriggerAlgo01();
                //logging algo result
                boolean algoResult = triggerAlgo.algo(quesEntity);
                Log.d("triggerAlgo", String.valueOf(algoResult));

                quesId = quesDao.getLastQuestionnaire();

                // test to get the quesEntity.id
                // Log.d("ques id", String.valueOf(quesId));

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                if(algoResult) {

                    dialog.setTitle(getString(R.string.after_ques_dialog_gotoexercise_title));
                    dialog.setMessage(getString(R.string.after_ques_dialog_gotoexercise_content));
                    dialog.setPositiveButton(getString(R.string.after_ques_dialog_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // show videos
                            Intent intent = new Intent();
                            intent.setClass(Questionaire_confirm.this, ExerciseVideoActivity.class);
                            // deliver with the questionaire number
                            intent.putExtra("quesId", quesId);
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                } else {
                    //show a dialog and go back to main page
                    dialog.setTitle(getString(R.string.after_ques_dialog_title));
                    dialog.setMessage(getString(R.string.after_ques_dialog_content));
                    dialog.setPositiveButton(getString(R.string.after_ques_dialog_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setClass(Questionaire_confirm.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                }

                break;

            case R.id.btn_query_all_ques:
                Log.d("divider", "############################");
                try {
                    int recordsNr = 0;
                    List<QuestionaireEntity> list = quesDao.queryAll();
                    for (QuestionaireEntity ques : list) {
                        recordsNr++;
                        Log.d("query_all_tag", ques.toString());
                    }
                    Utils.showToast(this, getString(R.string.you_have_finished) + recordsNr + getString(R.string.questionnaires));
                }catch (Exception e) {
                    Utils.showToast(this, e.getMessage());
                }
                break;
        }

    }

    private class MyTextWatcher implements TextWatcher {

        // a private EditText Object
        private EditText editText;

        public MyTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // update the clickMap for the 21th item in questionaire
            myClickMap.put(21, true);
            MyApplication.getInstance().clickMap.put(21, true);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // get the text inputed
            String str = editable.toString();
            // put the string into sharedpreferences
            editor.putString("ques21", str);
            editor.commit();
        }
    }

}