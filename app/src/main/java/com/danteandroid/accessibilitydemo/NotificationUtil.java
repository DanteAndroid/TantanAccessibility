package com.danteandroid.accessibilitydemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by yons on 17/11/7.
 */

public class NotificationUtil {
    public static final String CHANNEL_ID = "tantanAccess";
    public static final int NOTIFY_ID = 1;
    public static final String INTENT_FILTER = "notification_click";
    private static NotificationManager manager;

    public static void createNotification() {
        Intent bIntent = new Intent(INTENT_FILTER);
        PendingIntent intent = PendingIntent.getBroadcast(App.context, 0, bIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.context, CHANNEL_ID);
        Notification notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(App.context.getString(R.string.app_name))
                .setContentText("点击停止服务")
                .setContentIntent(intent)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();
        manager = (NotificationManager) App.context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFY_ID, notification);
        }
    }

    public static void removeNotification() {
        if (manager != null) {
            manager.cancel(NOTIFY_ID);
        }
    }
}
