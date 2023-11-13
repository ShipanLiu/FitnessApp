/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 30.05.23, 11:00
 *
 */

package com.example.a07.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Utils {
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale + 0.5f);
    }

    public static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:YYYY");
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentDateAndTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:YYYY HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    // hide keyboard
    public static void hideKeyboard(Activity act, View v) {
        // get InputMethodManager
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(v != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

    }

    public static void showToast(Context ctx, String desc) {
        Toast.makeText(ctx, desc, Toast.LENGTH_LONG).show();
    }

}

