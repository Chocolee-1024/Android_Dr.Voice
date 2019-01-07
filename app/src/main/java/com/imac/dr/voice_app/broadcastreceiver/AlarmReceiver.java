package com.imac.dr.voice_app.broadcastreceiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.module.AlarmConstantManager;
import com.imac.dr.voice_app.module.Preferences;
import com.imac.dr.voice_app.util.homepage.HomePageActivity;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    private String TAG = AlarmReceiver.class.getName();
    private Context mContext;
    private Preferences mPreferences;
    private AlarmManager mAlarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mAlarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        mPreferences = new Preferences(mContext);
        if (checkReboot(intent)) {
            if (mPreferences.getDailyOneRepeat()) {
                setNotifyDate(AlarmConstantManager.MODE_DAILY_ONE, mPreferences.getDailyTimeOneMillis(), AlarmConstantManager.ID_DAILY_ONE);
            }
            if (mPreferences.getDailyTwoRepeat()) {
                setNotifyDate(AlarmConstantManager.MODE_DAILY_TWO, mPreferences.getDailyTimeTwoMillis(), AlarmConstantManager.ID_DAILY_TWO);
            }
            if (mPreferences.getDailyThreeRepeat()) {
                setNotifyDate(AlarmConstantManager.MODE_DAILY_THREE, mPreferences.getDailyTimeThreeMillis(), AlarmConstantManager.ID_DAILY_THREE);
            }
            if (mPreferences.getWeeklyRepeat()) {
                setNotifyWeekly(AlarmConstantManager.MODE_WEEK, mPreferences.getWeeklyMillis(), AlarmConstantManager.ID_WEEKLY_ALARM);
            }
        } else {
            String mode = intent.getStringExtra(AlarmConstantManager.INTENT_MODE);
            Intent gotoMainActivity = new Intent(context, HomePageActivity.class);
            Bundle whichMde = new Bundle();
            gotoMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            whichMde.putString(AlarmConstantManager.INTENT_MODE, mode);
            gotoMainActivity.putExtras(whichMde);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, gotoMainActivity, PendingIntent.FLAG_ONE_SHOT);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            if (mode.equals(AlarmConstantManager.MODE_WEEK)) {
                String weekOfDay = intent.getStringExtra(AlarmConstantManager.WEEK_DAY);
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
                        .setContentText(context.getResources().getString(R.string.setting_daily_notice))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
                Notification notification = builder.build();
                manager.notify(AlarmConstantManager.ID_DAILY_ONE, notification);
            } else if (mode.equals(AlarmConstantManager.MODE_DAILY_TWO)) {
                builder.setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Voice 嗓音自我照護")
                        .setContentText(context.getResources().getString(R.string.setting_daily_notice))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
                Notification notification = builder.build();
                manager.notify(AlarmConstantManager.ID_DAILY_TWO, notification);
            } else if (mode.equals(AlarmConstantManager.MODE_DAILY_THREE)) {
                builder.setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Voice 嗓音自我照護")
                        .setContentText(context.getResources().getString(R.string.setting_daily_notice))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
                Notification notification = builder.build();
                manager.notify(AlarmConstantManager.ID_DAILY_THREE, notification);
            }
        }
    }

    private boolean checkReboot(Intent intent) {
        return "android.intent.action.BOOT_COMPLETED".equals(intent.getAction());
    }

    private boolean isWeeklyDay(String dayOnWeek) {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        Log.d(TAG, "week: " + week);
        Log.d(TAG, "input: " + dayOnWeek);
        return String.valueOf(week).equals(dayOnWeek);
    }

    private void setNotifyDate(String mode, long notifyDateTime, int id) {
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra(AlarmConstantManager.INTENT_MODE, mode);
        PendingIntent backAlarmIntent = PendingIntent.getBroadcast(mContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (null != backAlarmIntent) backAlarmIntent.cancel();
        backAlarmIntent = PendingIntent.getBroadcast(mContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notifyDateTime, 24 * 60 * 60 * 1000, backAlarmIntent);
        Log.d("tag", "setNotifyDate ");
    }

    private void setNotifyWeekly(String mode, long notifyDateTime, int id) {
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent backAlarmIntent = PendingIntent.getBroadcast(mContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (null != backAlarmIntent) backAlarmIntent.cancel();
        Bundle bundle = new Bundle();
        bundle.putString(AlarmConstantManager.INTENT_MODE, mode);
        bundle.putString(AlarmConstantManager.WEEK_DAY, String.valueOf(mPreferences.getWeeklyDay()));
        intent.putExtras(bundle);
        backAlarmIntent = PendingIntent.getBroadcast(mContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notifyDateTime, 24 * 60 * 60 * 1000, backAlarmIntent);
    }
}
