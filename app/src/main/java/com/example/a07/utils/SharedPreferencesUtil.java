/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 15.05.23, 16:32
 *
 */

package com.example.a07.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static SharedPreferencesUtil mUtil;

    private SharedPreferences preferences;

    public static SharedPreferencesUtil getInstance(Context context) {
        if(mUtil == null) {
            mUtil = new SharedPreferencesUtil();
            mUtil.preferences = context.getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE);
        }
        return mUtil;
    }


    /*
    * provide methods to check if this is the firsttime to open the app
    *
    * */

    public void writeBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean readBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }






}
