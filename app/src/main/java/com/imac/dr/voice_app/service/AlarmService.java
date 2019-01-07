package com.imac.dr.voice_app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.imac.dr.voice_app.broadcastreceiver.AlarmReceiver;
import com.imac.dr.voice_app.module.AlarmConstantManager;
import com.imac.dr.voice_app.module.Preferences;

import java.util.Calendar;

/**
 * Created by flowmaHuang on 2016/10/19.
 */

public class AlarmService extends Service {
    private AlarmManager mAlarmManager;
    private Preferences mPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mPreferences = new Preferences(AlarmService.this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            Log.e("service", "intent was null, flags=" + flags + " bits=" + Integer.toBinaryString(flags));
            return START_REDELIVER_INTENT;
        }
        int action = intent.getIntExtra(AlarmConstantManager.ACTION_MODE, AlarmConstantManager.ACTION_CLOSE);
        if (action == AlarmConstantManager.ACTION_OPEN) {
            setAlarmOpen(intent.getStringExtra(AlarmConstantManager.INTENT_MODE));
        } else {
            setAlarmClose(intent.getStringExtra(AlarmConstantManager.INTENT_MODE));
        }
        Log.e("service", "action start");
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setAlarmOpen(String type) {
        Calendar calendar = Calendar.getInstance();
        Intent mAlarmIntent = new Intent(AlarmService.this, AlarmReceiver.class);
        PendingIntent pendingIntent;

        int settingHour;
        int settingMin;
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMin = calendar.get(Calendar.MINUTE);

        if (type.equals(AlarmConstantManager.MODE_DAILY)) {
            mAlarmIntent.putExtra(AlarmConstantManager.INTENT_MODE, AlarmConstantManager.MODE_DAILY);
            settingHour = Integer.valueOf(mPreferences.getDailyHour());
            settingMin = Integer.valueOf(mPreferences.getDailyMin());
            pendingIntent = PendingIntent.getBroadcast(this, AlarmConstantManager.ID_DAILY_ALARM, mAlarmIntent, PendingIntent.FLAG_ONE_SHOT);
        } else {
            mAlarmIntent.putExtra(AlarmConstantManager.INTENT_MODE, AlarmConstantManager.MODE_WEEK);
            mAlarmIntent.putExtra(AlarmConstantManager.WEEK_DAY, mPreferences.getWeeklyDay());
            settingHour = Integer.valueOf(mPreferences.getWeeklyHour());
            settingMin = Integer.valueOf(mPreferences.getWeeklyMin());
            pendingIntent = PendingIntent.getBroadcast(this, AlarmConstantManager.ID_WEEKLY_ALARM, mAlarmIntent, PendingIntent.FLAG_ONE_SHOT);
        }

        calendar.set(Calendar.HOUR_OF_DAY, settingHour);
        calendar.set(Calendar.MINUTE, settingMin);
        calendar.set(Calendar.SECOND, 0);
        if (nowHour >= settingHour && nowMin >= settingMin) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                86400000, pendingIntent);
    }

    private void setAlarmClose(String type) {
        Intent intent = new Intent(AlarmService.this, AlarmReceiver.class);
        PendingIntent pendingIntent;
        if (type.equals(AlarmConstantManager.MODE_DAILY)) {
            pendingIntent = PendingIntent.getBroadcast(this, AlarmConstantManager.ID_DAILY_ALARM, intent, PendingIntent.FLAG_ONE_SHOT);
        } else {
            pendingIntent = PendingIntent.getBroadcast(this, AlarmConstantManager.ID_WEEKLY_ALARM, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        mAlarmManager.cancel(pendingIntent);
    }
}
