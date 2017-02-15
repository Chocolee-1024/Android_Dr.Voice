package com.imac.voice_app.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.imac.voice_app.R;
import com.imac.voice_app.module.AlarmConstantManager;
import com.imac.voice_app.util.homepage.HomePageActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {
    private String TAG = AlarmReceiver.class.getName();

    // TODO: 2017/2/13    intent 收不到資料
    @Override
    public void onReceive(Context context, Intent intent) {
        String mode = intent.getStringExtra(AlarmConstantManager.INTENT_MODE);
        int weekOfDay = intent.getIntExtra("AAA", 0);
        Intent gotoMainActivity = new Intent(context, HomePageActivity.class);
        Bundle whichMde = new Bundle();
        gotoMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        whichMde.putString(AlarmConstantManager.INTENT_MODE, mode);
        gotoMainActivity.putExtras(whichMde);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, gotoMainActivity, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        if (mode.equals(AlarmConstantManager.MODE_WEEK)) {
            if (isWeeklyDay(weekOfDay)) {
                builder.setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Voice 嗓音自我照護")
                        .setContentText(context.getResources().getString(R.string.setting_weekly_notice))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
                Notification notification = builder.build();
                manager.notify(AlarmConstantManager.ID_WEEKLY_ALARM, notification);
            }
        } else if (mode.equals(AlarmConstantManager.MODE_DAILY)) {
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Voice 嗓音自我照護")
                    .setContentText(context.getResources().getString(R.string.setting_daily_notice))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            Notification notification = builder.build();
            manager.notify(AlarmConstantManager.ID_DAILY_ALARM, notification);
        } else if (mode.equals(AlarmConstantManager.MODE_DAILY_ONE)) {
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Voice 嗓音自我照護")
                    .setContentText("1")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            Notification notification = builder.build();
            manager.notify(AlarmConstantManager.ID_DAILY_ONE, notification);
        } else if (mode.equals(AlarmConstantManager.MODE_DAILY_TWO)) {
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Voice 嗓音自我照護")
                    .setContentText("2")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            Notification notification = builder.build();
            manager.notify(AlarmConstantManager.ID_DAILY_TWO, notification);
        } else if (mode.equals(AlarmConstantManager.MODE_DAILY_THREE)) {
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Voice 嗓音自我照護")
                    .setContentText("3")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            Notification notification = builder.build();
            manager.notify(AlarmConstantManager.ID_DAILY_THREE, notification);
        }
    }

    private String milliSecondFormat(long milliSecond) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSecond);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return format.format(calendar.getTime());
    }

    private boolean isWeeklyDay(int dayOnWeek) {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        Log.d(TAG, "week: " + week);
        Log.d(TAG, "input: " + dayOnWeek);
        return dayOnWeek == week;
    }
}
