package com.imac.voice_app.util.setting;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import com.imac.voice_app.R;
import com.imac.voice_app.broadcastreceiver.AlarmReceiver;
import com.imac.voice_app.component.ToolbarView;
import com.imac.voice_app.core.ActivityLauncher;
import com.imac.voice_app.module.AlarmConstantManager;
import com.imac.voice_app.module.AlarmPreferences;
import com.imac.voice_app.module.SharePreferencesManager;
import com.imac.voice_app.service.AlarmService;
import com.imac.voice_app.util.homepage.HomePageActivity;
import com.imac.voice_app.util.login.LoginActivity;
import com.imac.voice_app.view.setting.SettingView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.imac.voice_app.module.AlarmConstantManager.KEY_BACK_DATA;

/**
 * Setting page activity
 * Created by flowmaHuang on 2016/9/21.
 */
public class SettingActivity extends Activity {
    private SettingView mSettingLayout;
    private AlertDialog mWeekPickDialog;
    private AlarmPreferences mAlarmPreferences;
    private static boolean logOpen = true;
    private ArrayList<String> remindData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getBundle();
        startAlert();
        init();
        firstUseSetting();
    }

    private void getBundle() {
        remindData = (ArrayList<String>) getIntent().getExtras().getSerializable(LoginActivity.KEY_SETTING);
    }

    private void init() {
        mAlarmPreferences = new AlarmPreferences(this);
        mSettingLayout = new SettingView(this, settingViewCallBack(), mAlarmPreferences);
        mWeekPickDialog = getMultiItemDialog();
        mSettingLayout.setRemindData(remindData);
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

    private void startAlert() {
        String[] backToClinicArray = remindData.get(0).split("[,/]+");
        String[] treatmentArray = remindData.get(1).split("[,/]+");
        boolean isBackNone = true;
        for (String index : backToClinicArray) {
            isBackNone &= "0".equals(index);
        }
        boolean isTreatmentNone = true;
        for (String index : treatmentArray) {
            isTreatmentNone &= "0".equals(index);
        }
        if (isBackNone && isTreatmentNone) return;
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(RemindService.KEY_REMIND, remindData);
//        Intent intent = new Intent();
//        intent.putExtras(bundle);
//        intent.setClass(this, RemindService.class);
//        startService(intent);
        //===============================================
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long backTimeInMillis = setBackToClinicCalendar();
        if (backTimeInMillis != 0) {
            Intent backIntent = new Intent(this, AlarmReceiver.class);
            backIntent.putExtra(AlarmConstantManager.INTENT_MODE, AlarmConstantManager.MODE_BACK);
            backIntent.putExtra(KEY_BACK_DATA, remindData.get(0));
            PendingIntent backAlarmIntent = PendingIntent.getBroadcast(this, AlarmConstantManager.ID_BACK, backIntent, PendingIntent.FLAG_ONE_SHOT);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, backTimeInMillis, backAlarmIntent);
        }
        long treatmentTimeInMillis = setTreatmentYearCalendar();
        if (treatmentTimeInMillis != 0) {
            Intent treatmentIntent = new Intent(this, AlarmReceiver.class);
            treatmentIntent.putExtra(AlarmConstantManager.INTENT_MODE, AlarmConstantManager.MODE_TREATMENT);
            treatmentIntent.putExtra(AlarmConstantManager.KEY_TREATMENT_DATA, remindData.get(1));
            PendingIntent treatmentAlarmIntent = PendingIntent.getBroadcast(this, AlarmConstantManager.ID_TREATMENT, treatmentIntent, PendingIntent.FLAG_ONE_SHOT);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, treatmentTimeInMillis, treatmentAlarmIntent);
        }
    }

    private long setBackToClinicCalendar() {
        String[] backToClinicArray = remindData.get(0).split("[,/]+");
        boolean isNone = true;
        for (String index : backToClinicArray) {
            isNone &= "0".equals(index);
            if (isNone) return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(backToClinicArray[0]));
        calendar.set(Calendar.MONTH, Integer.valueOf(backToClinicArray[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(backToClinicArray[2]) - 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    private long setTreatmentYearCalendar() {
        String[] treatmentArray = remindData.get(1).split("[,/]+");
        boolean isNone = true;
        for (String index : treatmentArray) {
            isNone &= "0".equals(index);
            if (isNone) return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(treatmentArray[0]));
        calendar.set(Calendar.MONTH, Integer.valueOf(treatmentArray[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(treatmentArray[2]) - 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    private boolean isPackageInstalled(String packagename) {
        PackageManager packageManager = getPackageManager();
        try {
            packageManager.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /************
     * Callback Func
     ***********/
    private SettingView.settingRepeatCallBack settingViewCallBack() {
        return new SettingView.settingRepeatCallBack() {
            @Override
            public void setLogout() {
                SharePreferencesManager sharePreferencesManager = SharePreferencesManager.getInstance(SettingActivity.this);
                sharePreferencesManager.clearAll();
                ActivityLauncher.go(SettingActivity.this, HomePageActivity.class, null);
            }

            private static final String INTENT_TYPE = "plain/text";
            private static final String PACKAGE_NAME = "com.google.android.gm";
            private static final String CLASS_NAME = "com.google.android.gm.ComposeActivityGmail";

            @Override
            public void setSendMail() {
                if (!isPackageInstalled(PACKAGE_NAME)) {
                    Toast.makeText(SettingActivity.this, "請確認是否安裝Gmail", Toast.LENGTH_SHORT).show();
                    return;
                }
//                Intent sendMail = new Intent(Intent.ACTION_VIEW);
//                sendMail.putExtra(Intent.EXTRA_EMAIL, new String[]{"voice.dr.wang@gmail.com"});
//                sendMail.setType(INTENT_TYPE);
//                sendMail.setClassName(PACKAGE_NAME, CLASS_NAME);
//                startActivity(Intent.createChooser(sendMail,"send"));
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","voice.dr.wang@gmail.com", null));
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

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