package com.example.expiryalert;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

public class AlarmBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String text = null;
        if (bundle != null) {
            text = bundle.getString("event");
        }
        String date = null;
        if (bundle != null) {
            date = bundle.getString("date") + " " + bundle.getString("time");
        }

        //Click on Notification
        Intent intent1 = new Intent(context, NotificationMessage.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("message", text);

        //Notification Builder
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");


        //here we set all the properties for the notification
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        contentView.setImageViewResource(R.id.icon, R.drawable.ic_launcher_background);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);
        contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent);
        contentView.setTextViewText(R.id.message, text);
        contentView.setTextViewText(R.id.date, date);
        mBuilder.setSmallIcon(R.drawable.alarm);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        mBuilder.setOnlyAlertOnce(true);
//        mBuilder.build().flags = NotificationCompat.FLAG_ONGOING_EVENT;
        mBuilder.setContent(contentView);
        mBuilder.setContentIntent(pendingIntent);

        //we have to create notification channel after api level 26
        String channelId = "channel_id";
        NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
        channel.enableVibration(true);
        notificationManager.createNotificationChannel(channel);
        mBuilder.setChannelId(channelId);

        Notification notification = mBuilder.build();
        notificationManager.notify(1, notification);


    }
}