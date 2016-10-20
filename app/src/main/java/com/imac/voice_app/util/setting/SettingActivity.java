package com.imac.voice_app.util.setting;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import com.imac.voice_app.R;
import com.imac.voice_app.broadcastreceiver.AlarmReceiver;
import com.imac.voice_app.component.ToolbarView;
import com.imac.voice_app.module.AlarmConstantManager;
import com.imac.voice_app.module.AlarmPreferences;
import com.imac.voice_app.service.AlarmService;
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
    private AlarmPreferences mAlarmPreferences;

    private static boolean logOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        firstUseSetting();
    }

    private void init() {
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
                        setAlarmManagerOpen(AlarmConstantManager.MODE_DAILY);
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
                        setAlarmManagerOpen(AlarmConstantManager.MODE_WEEK);
                    }
                    showLog("Time Picker", "Weekly" + "/" + hour + ":" + min);
                }
            };
        }

        TimePickerDialog mTimePickerDialog = new TimePickerDialog(SettingActivity.this, listener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        mTimePickerDialog.show();
    }

    private void setAlarmManagerOpen(String type) {
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra(AlarmConstantManager.ACTION_MODE, AlarmConstantManager.ACTION_OPEN);
        intent.putExtra(AlarmConstantManager.INTENT_MODE, type);
        startService(intent);

        showLog("Alarm Open", type);
    }

    private void setAlarmManagerClose(String type) {
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra(AlarmConstantManager.ACTION_MODE, AlarmConstantManager.ACTION_CLOSE);
        intent.putExtra(AlarmConstantManager.INTENT_MODE, type);
        startService(intent);
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
            public void setLogout() {

            }

            @Override
            public void setSendMail() {
                Intent sendMail = new Intent(android.content.Intent.ACTION_SEND);
                sendMail.putExtra(Intent.EXTRA_EMAIL, new String[]{"voice.dr.wang@gmail.com"});
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
                    setAlarmManagerOpen(AlarmConstantManager.MODE_DAILY);
                } else {
                    setAlarmManagerClose(AlarmConstantManager.MODE_DAILY);
                }
            }

            @Override
            public void setWeeklyRepeat(boolean isChecked) {
                mAlarmPreferences.saveWeeklyRepeat(isChecked);
                showLog("Weekly Switch", String.valueOf(isChecked));
                if (isChecked) {
                    setAlarmManagerOpen(AlarmConstantManager.MODE_WEEK);
                } else {
                    setAlarmManagerClose(AlarmConstantManager.MODE_WEEK);
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