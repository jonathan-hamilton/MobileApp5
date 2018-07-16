package com.example.mobileapp_working;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jonathan Hamilton on 3/29/2018.
 */

public class Alert extends Activity {

    private final int NOTIFICATION_ID = 1004;
    private String alertType;

    public static final String NOTIFICATION_CHANNEL = "4444";
    CharSequence NOTIFICATION_CHANNEL_NAME = "alert_channel";
    int importantce = NotificationManager.IMPORTANCE_LOW;
    TextView alertInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window window = getWindow();
        Log.d("Alert", "called. oncreate");
        window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );

        setContentView(R.layout.activity_alert_set);
        alertInfo = findViewById(R.id.alert_info);

        Intent intent = getIntent();
        long alertId = intent.getLongExtra("com.example.mobileapp_working.alertId", 0);

        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM alerts WHERE _id = " + alertId + ";"
                ,null
                ,null);

        if(result != null){
            result.moveToFirst();
        }

        alertInfo.setText(
                result.getString(3) + " Alert, " +
                result.getString(2));

        addNotification();

    }

    private void addNotification() {
        NotificationChannel notificationChannel = new NotificationChannel(
                NOTIFICATION_CHANNEL,
                NOTIFICATION_CHANNEL_NAME,
                importantce
                );
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300});


        NotificationManager notificationMgr = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationMgr.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this,
                NOTIFICATION_CHANNEL);
        builder.setDefaults(Notification.DEFAULT_ALL)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300})
                .setSound(null)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                .setContentIntent(pendnigIntent);

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                this,
//                NOTIFICATION_CHANNEL);
//            builder.setDefaults(Notification.DEFAULT_ALL)
//                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300})
//                    .setSound(null)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);


//                working notification - has 'null' channel toast message
//        Notification notification = new Notification.Builder(this)
//                .setContentTitle("Wake up")
//                .setSmallIcon(android.R.drawable.star_on)
//                .setAutoCancel(false)
//                .build();
//        notification.defaults|= Notification.DEFAULT_SOUND;
//        notification.defaults|= Notification.DEFAULT_LIGHTS;
//        notification.defaults|= Notification.DEFAULT_VIBRATE;
//        notification.flags |= Notification.FLAG_INSISTENT;
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        notificationMgr.notify(NOTIFICATION_ID, notification);



//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent repeatingIntent = new Intent(context, AlertBroadcast.class);
//        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                context,
//                100,
//                repeatingIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                .setContentIntent(pendingIntent)
//                .setSmallIcon(android.R.drawable.arrow_up_float)
//                .setContentTitle("Title")
//                .setContentText("Text Info")
//                .setAutoCancel(true);
//
//            notificationManager.notify(100, builder.build());

//    }
//        if (AlertList.ALERT_TYPE.equals("Course Start")) {
//
//            Toast.makeText(context, "START ALERT RUNNING BAM!",
//                    Toast.LENGTH_LONG).show();
//
//        } else if (AlertList.ALERT_TYPE.equals("Course End")) {
//
//            Toast.makeText(context, "END ALERT RUNNING BAM!",
//                    Toast.LENGTH_LONG).show();
//
//        } else if (AlertList.ALERT_TYPE.equals("Objective")) {
//
//            Toast.makeText(context, "OBJECTIVE ALERT RUNNING BAM!",
//                    Toast.LENGTH_LONG).show();
//
//        } else if (AlertList.ALERT_TYPE.equals("Performance")) {
//
//            Toast.makeText(context, "PERFORMANCE ALERT RUNNING BAM!",
//                    Toast.LENGTH_LONG).show();
//
//        } else{
//
//            Toast.makeText(context, "Something went wrong there's no ALERT TYPE selected",
//                    Toast.LENGTH_LONG).show();
//        }
//        AlertList.ALERT_TYPE = null;
    }
}
