

/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 25.07.23, 16:45
 *
 */

package com.example.a07;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    String notificationText;

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notification_id", 0);  // Get id from Intent

        notificationText = "";
        switch (notificationId) {
            case 1:
                notificationText = "Click to start the morning Questionnaire.";
                break;
            case 2:
                notificationText = "Click to start the noon Questionnaire.";
                break;
            case 3:
                notificationText = "Click to start the late noon Questionnaire.";
                break;
        }

        // Intent to launch activity
        Intent activityIntent = new Intent(context, IntroductoryActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activityIntent.putExtra("runFunction", "openQuestionnaire"); // Pass the function to be executed as an extra
        PendingIntent activityPendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "questionnaireNotification")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Time for a Questionnaire!") //! TODO: Change this to a string resource
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);  // removes notification when clicked

        // Set the intent to launch activity
        builder.setContentIntent(activityPendingIntent);

        // Get an instance of NotificationManager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Show the notification
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }
}

