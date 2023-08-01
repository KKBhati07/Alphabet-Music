package com.example.alphabetmusic;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ApplicationClass extends Application {
    public static final String CHANNEL_ID_1="CHANNEL_1";
    public static final String CHANNEL_ID_2="CHANNEL_2";
    public static final String ACTION_PREVIOUS="PREVIOUS";
    public static final String ACTION_NEXT="NEXT";
    public static final String ACTION_PLAY_PAUSE="PLAY_PAUSE";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    //creating notification to show which song is playing
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel1=new NotificationChannel(CHANNEL_ID_1,"ChanneL One", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel1.setDescription("Channel ! Description");
            NotificationChannel notificationChannel2=new NotificationChannel(CHANNEL_ID_2,"ChanneL Two", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel2.setDescription("Channel @ Description");
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel1);
            notificationManager.createNotificationChannel(notificationChannel2);

        }
    }
}
