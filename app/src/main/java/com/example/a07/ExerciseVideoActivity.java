/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 08:04
 *
 */

package com.example.a07;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.example.a07.dao.QuesDao;

import java.util.Random;

public class ExerciseVideoActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private VideoView videoView;
    private int[] videoResources = {R.raw.sample_exercise1, R.raw.sample_exercise_2};

    // this is important for updating the questionaire database
    private int targetQuesId;
    // triggered
    private Boolean myTriggered = true;
    // video number
    private int videoNr;
    // the seekbar
    private int myExerciseScore = 50;
    // the comment
    private String myComment = "I have nothing to say";

    private SeekBar mySeekBar;
    private EditText myEditText;
    private Button myUpdateAndSaveBtn;

    private QuesDao quesDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_video);

        videoView = findViewById(R.id.videoView);

        // Add media controls (play/pause/seekbar)
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);   //set the position of the control bar

        playRandomVideo();  //play random video from video resource
        findViewById(R.id.btn_quit_video).setOnClickListener(this);

        // get the targetQuesId
        Intent intent = getIntent();
        targetQuesId = intent.getIntExtra("quesId", 0);
        Log.d("targetQuesId", Integer.toString(targetQuesId));

        mySeekBar = findViewById(R.id.my_exercise_review_seekbar);
        myEditText = findViewById(R.id.my_exercise_review_edittext);
        myUpdateAndSaveBtn = findViewById(R.id.btn_update_and_save);
        myUpdateAndSaveBtn.setOnClickListener(this);

        quesDao = MyApplication.getInstance().getAppDatebase().quesDao();

    }

    private void playRandomVideo(){
        int randomIndex = new Random().nextInt(videoResources.length);      //random number between 0 and length-1
        videoNr = randomIndex + 1;
        int videoResource = videoResources[randomIndex];
        String videoPath = "android.resource://" + getPackageName() + "/" + videoResource;
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_quit_video:
                // show a dialog to get the grading of this video
                showDialogSeekBarAndComment();
                // let this btn disappear
                findViewById(R.id.btn_quit_video).setVisibility(View.GONE);

                break;

            case R.id.btn_update_and_save:

                // update to database
                quesDao.updateQuestionnaire(targetQuesId, myTriggered, videoNr, myExerciseScore, myComment);


                Intent intent = new Intent();
                intent.setClass(ExerciseVideoActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void showDialogSeekBarAndComment() {

        LinearLayout layout = findViewById(R.id.exercise_comment_gone);
        layout.setVisibility(View.VISIBLE);


        mySeekBar.setOnSeekBarChangeListener(this);
        myEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                myComment = editable.toString();
            }
        });

            Log.d("last_id", String.valueOf(targetQuesId));
            Log.d("last_triggered", String.valueOf(myTriggered));
            Log.d("last_videoNr", String.valueOf(videoNr));
            Log.d("last_seekbar", String.valueOf(myExerciseScore));
            Log.d("last_comment", String.valueOf(myComment));


    }






//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
//        View mView = getLayoutInflater().inflate(R.layout.dialog_seekbar_and_comment, null);
//        // get seekBar and EditText
//        SeekBar mySeekBar = mView.findViewById(R.id.my_exercise_review_seekbar);
//        mySeekBar.setOnSeekBarChangeListener(ExerciseVideoActivity.this);
//
//         //get the EditText
//        EditText editText = mView.findViewById(R.id.my_exercise_review_edittext);
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                // Do nothing
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                // Do nothing
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                myComment = editable.toString();
//            }
//        });
//
//
//
//        mBuilder.setTitle(R.string.ques_dialog_title);
//        //put the mView into the dialog
//        mBuilder.setView(mView);
//
//        mBuilder.setPositiveButton(R.string.ques_dialog_yes, (dialog, which) -> {
//
//            // test
//            Log.d("last_id", String.valueOf(targetQuesId));
//            Log.d("last_triggered", String.valueOf(myTriggered));
//            Log.d("last_videoNr", String.valueOf(videoNr));
//            Log.d("last_seekbar", String.valueOf(mySeekBar));
//            Log.d("last_comment", String.valueOf(myComment));
//
//            // update the questionare  <==  triggered, videoNr, seekbar, comment
//
//
//        });
//
//        //show the AlertDialog using show() method
//        AlertDialog exerciseAlertDialog = mBuilder.create();
//        exerciseAlertDialog.show();



    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        myExerciseScore = seekBar.getProgress();
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}