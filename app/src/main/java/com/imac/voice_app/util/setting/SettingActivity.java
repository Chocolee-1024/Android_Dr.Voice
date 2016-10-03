package com.imac.voice_app.util.setting;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import com.imac.voice_app.R;
import com.imac.voice_app.broadcastreceiver.AlarmReceiver;
import com.imac.voice_app.component.ToolbarView;
import com.imac.voice_app.module.AlarmPreferences;
import com.imac.voice_app.view.setting.SettingView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Setting page activity
 * Created by flowmaHuang on 2016/9/21.
 */
public class SettingActivity extends Activity {
    private SettingView mSettingLayout;
    private AlertDialog mWeekPickDialog;
    private AlarmManager mAlarmManager;
    private AlarmPreferences mAlarmPreferences;

    private final String MODE_WEEK = AlarmReceiver.MODE_WEEK;
    private final String MODE_DAILY = AlarmReceiver.MODE_DAILY;
    private final int NOTIFICATION_ID_DAILY = AlarmReceiver.ID_DAILY_ALARM;
    private final int NOTIFICATION_ID_WEEKLY = AlarmReceiver.ID_WEEKLY_ALARM;

    private static boolean logOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        firstUseSetting();
    }

    private void init() {
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mAlarmPreferences = new AlarmPreferences(this);
        mSettingLayout = new SettingView(this, settingViewCallBack(), mAlarmPreferences);
        mWeekPickDialog = getMultiItemDialog();
        mSettingLayout.setToolbarViewCallBack(toolbarCallBack());
    }

    private void firstUseSetting() {
        if (mAlarmPreferences.getDailyHour().equals("")) {
            mAlarmPreferences.saveDailyTime("00", "00");
        }
        if (mAlarmPreferences.getWeeklyHour().equals("")) {
            mAlarmPreferences.saveWeeklyTime("00", "00");
        }
    }

    private void setTime(int id) {
        GregorianCalendar calendar = new GregorianCalendar();
        TimePickerDialog.OnTimeSetListener listener;
        if (id == R.id.tv_daily_notice_time) {
            listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String hour = (hourOfDay >= 10) ? String.valueOf(hourOfDay) : "0" + String.valueOf(hourOfDay);
                    String min = (minute >= 10) ? String.valueOf(minute) : "0" + String.valueOf(minute);
                    String time = hour + " : " + min;

                    mSettingLayout.setDailyTimeTextView(time);
                    mAlarmPreferences.saveDailyTime(hour, min);

                    if (mAlarmPreferences.getDailyRepeat()) {
                        setAlarmManagerOpen(MODE_DAILY);
                    }
                    showLog("Time Picker", "Daily" + "/" + hour + ":" + min);
                }
            };
        } else {
            listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String hour = (hourOfDay >= 10) ? String.valueOf(hourOfDay) : "0" + String.valueOf(hourOfDay);
                    String min = (minute >= 10) ? String.valueOf(minute) : "0" + String.valueOf(minute);
                    String time = hour + " : " + min;

                    mSettingLayout.setWeeklyTimeTextView(time);
                    mAlarmPreferences.saveWeeklyTime(hour, min);

                    if (mAlarmPreferences.getWeeklyRepeat()) {
                        setAlarmManagerOpen(MODE_WEEK);
                    }
                    showLog("Time Picker" , "Weekly" + "/" + hour + ":" + min);
                }
            };
        }

        TimePickerDialog mTimePickerDialog = new TimePickerDialog(SettingActivity.this, listener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        mTimePickerDialog.show();
    }

    private void setAlarmManagerOpen(String type) {
        Intent intent = new Intent(SettingActivity.this, AlarmReceiver.class);
        Calendar calendar = Calendar.getInstance();
        PendingIntent pendingIntent;
        int settingHour;
        int settingMin;
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMin = calendar.get(Calendar.MINUTE);
        if (type.equals(MODE_DAILY)) {
            intent.putExtra(AlarmReceiver.INTENT_MODE, MODE_DAILY);
            settingHour = Integer.valueOf(mAlarmPreferences.getDailyHour());
            settingMin = Integer.valueOf(mAlarmPreferences.getDailyMin());
            pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID_DAILY, intent, PendingIntent.FLAG_ONE_SHOT);
        } else {
            intent.putExtra(AlarmReceiver.INTENT_MODE, MODE_WEEK);
            intent.putExtra(AlarmReceiver.WEEK_DAY, mAlarmPreferences.getWeeklyDay());
            settingHour = Integer.valueOf(mAlarmPreferences.getWeeklyHour());
            settingMin = Integer.valueOf(mAlarmPreferences.getWeeklyMin());
            pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID_WEEKLY, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        calendar.set(Calendar.HOUR_OF_DAY, settingHour);
        calendar.set(Calendar.MINUTE, settingMin);
        if (nowHour >= settingHour && nowMin >= settingMin) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                86400000, pendingIntent);

        showLog("Alarm Open", type);
    }

    private void setAlarmManagerClose(String type) {
        Intent intent = new Intent(SettingActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent;
        if (type.equals(MODE_DAILY)) {
            pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID_DAILY, intent, PendingIntent.FLAG_ONE_SHOT);
        } else {
            pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID_WEEKLY, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        mAlarmManager.cancel(pendingIntent);
        showLog("Alarm Close", type);
    }

    private AlertDialog getMultiItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.week_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String day = (String) getResources().getTextArray(R.array.week_array)[which];
                mSettingLayout.setWeeklyWeekTextView(day);
                mAlarmPreferences.saveWeeklyDay(which);

                showLog("Day of Week", ":" + which);
            }
        });
        return builder.create();
    }

    private void showLog(String tag, String description) {
        if (logOpen) {
            Log.e(tag, description);
        }
    }

    /************
     * Callback Func
     ***********/
    private SettingView.settingRepeatCallBack settingViewCallBack() {
        return new SettingView.settingRepeatCallBack() {
            @Override
            public void setSendMail() {
                Intent sendMail = new Intent(android.content.Intent.ACTION_SEND);
                sendMail.putExtra(Intent.EXTRA_EMAIL,new String[] {"wong1123123@gmail.com"});
                sendMail.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");
                sendMail.setType("message/rfc822");
                sendMail.setClassName("com.google.android.gm",
                        "com.google.android.gm.ComposeActivityGmail");
                startActivity(sendMail);

            }

            @Override
            public void setDailyRepeat(boolean isChecked) {
                mAlarmPreferences.saveDailyRepeat(isChecked);
                showLog("Daily Switch", String.valueOf(isChecked));
                if (isChecked) {
                    setAlarmManagerOpen(MODE_DAILY);
                } else {
                    setAlarmManagerClose(MODE_DAILY);
                }
            }

            @Override
            public void setWeeklyRepeat(boolean isChecked) {
                mAlarmPreferences.saveWeeklyRepeat(isChecked);
                showLog("Weekly Switch", String.valueOf(isChecked));
                if (isChecked) {
                    setAlarmManagerOpen(MODE_WEEK);
                } else {
                    setAlarmManagerClose(MODE_WEEK);
                }
            }

            @Override
            public void setDailyAlarmTime(int id) {
                setTime(id);
            }

            @Override
            public void setWeeklyAlarmTime(int id) {
                setTime(id);
            }

            @Override
            public void setWeeklyAlarmDay() {
                mWeekPickDialog.show();
            }
        };
    }

    private ToolbarView.toolbarCallBack toolbarCallBack() {
        return new ToolbarView.toolbarCallBack() {
            @Override
            public void backButtonListener() {
                finish();
            }

            @Override
            public void menuButtonListener() {
                finish();
            }
        };
    }
}