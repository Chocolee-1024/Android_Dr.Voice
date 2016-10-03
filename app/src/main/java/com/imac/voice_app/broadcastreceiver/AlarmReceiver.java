package com.imac.voice_app.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.imac.voice_app.R;
import com.imac.voice_app.util.mainmenu.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String INTENT_MODE = "mode";
    public static final String WEEK_DAY = "week_day";
    public static final String MODE_WEEK = "mode_week";
    public static final String MODE_DAILY = "mode_daily";
    public static final int ID_DAILY_ALARM = 0x01;
    public static final int ID_WEEKLY_ALARM = 0x10;

    @Override
    public void onReceive(Context context, Intent intent) {
        String mode = intent.getStringExtra(INTENT_MODE);
        Intent gotoMainActivity = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, gotoMainActivity, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        if (mode.equals(MODE_WEEK)) {
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Voice 嗓音自我照護")
                    .setContentText(context.getResources().getString(R.string.setting_weekly_notice))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
        } else if (mode.equals(MODE_DAILY)) {
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Voice 嗓音自我照護")
                    .setContentText(context.getResources().getString(R.string.setting_daily_notice))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
        }

        Notification notification = builder.build();
        manager.notify(0x01, notification);
    }
}
