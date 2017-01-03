package com.imac.voice_app.util.setting;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.imac.voice_app.R;
import com.imac.voice_app.broadcastreceiver.AlarmReceiver;
import com.imac.voice_app.component.ToolbarView;
import com.imac.voice_app.core.ActivityLauncher;
import com.imac.voice_app.module.AlarmConstantManager;
import com.imac.voice_app.module.CalendarIntentHelper;
import com.imac.voice_app.module.Preferences;
import com.imac.voice_app.service.AlarmService;
import com.imac.voice_app.util.doctorsetting.DoctorSettingActivity;
import com.imac.voice_app.util.homepage.HomePageActivity;
import com.imac.voice_app.view.setting.SettingView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Setting page activity
 * Created by flowmaHuang on 2016/9/21.
 */
public class SettingActivity extends Activity {
    private SettingView mSettingLayout;
    private AlertDialog mWeekPickDialog;
    private Preferences mPreferences;
    private static boolean logOpen = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        firstUseSetting();
    }


    private void init() {
        mPreferences = new Preferences(this);
        mSettingLayout = new SettingView(this, settingViewCallBack(), mPreferences);
        mWeekPickDialog = getMultiItemDialog();
        mSettingLayout.setToolbarViewCallBack(toolbarCallBack());
    }

    private void firstUseSetting() {
        if (mPreferences.getDailyHour().equals("")) {
            mPreferences.saveDailyTime("00", "00");
        }
        if (mPreferences.getWeeklyHour().equals("")) {
            mPreferences.saveWeeklyTime("00", "00");
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
                    mPreferences.saveDailyTime(hour, min);

                    if (mPreferences.getDailyRepeat()) {
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
                    mPreferences.saveWeeklyTime(hour, min);

                    if (mPreferences.getWeeklyRepeat()) {
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
                mPreferences.saveWeeklyDay(which);

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

    private void setNotifilyDate(String isBackTime, long notifilyDateTime) {
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (isBackTime == Preferences.SP_BACK_TIME) {
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra(AlarmConstantManager.INTENT_MODE, AlarmConstantManager.MODE_BACK);
            intent.putExtra(AlarmConstantManager.KEY_BACK_DATA, notifilyDateTime);
            Log.d("setNotifilyDate", "setNotifilyDate: " + notifilyDateTime);
            PendingIntent backAlarmIntent = PendingIntent.getBroadcast(this, AlarmConstantManager.ID_BACK, intent, PendingIntent.FLAG_ONE_SHOT);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, notifilyDateTime, backAlarmIntent);
        } else if (isBackTime == Preferences.SP_TREATMENT_TIME) {
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra(AlarmConstantManager.INTENT_MODE, AlarmConstantManager.MODE_TREATMENT);
            intent.putExtra(AlarmConstantManager.KEY_TREATMENT_DATA, notifilyDateTime);
            Log.d("setNotifilyDate", "setNotifilyDate: " + notifilyDateTime);
            PendingIntent treatmentAlarmIntent = PendingIntent.getBroadcast(this, AlarmConstantManager.ID_TREATMENT, intent, PendingIntent.FLAG_ONE_SHOT);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, notifilyDateTime, treatmentAlarmIntent);
        } else throw new IllegalArgumentException("Argument error");

    }

    private DatePickerDialog.OnDateSetListener onDateSetListener(final String isBackTime, final TextView yearText, final TextView monthText, final TextView dayText, final TextView weekText) {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE");
                CalendarIntentHelper calIntent = new CalendarIntentHelper();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth - 1);
                String week = weekFormat.format(calendar.getTime());

                yearText.setText(String.valueOf(year - 1911));
                monthText.setText(String.valueOf(month + 1));
                dayText.setText(String.valueOf(dayOfMonth));
                weekText.setText(week);

//                setNotifilyDate(isBackTime, calendar.getTimeInMillis());
                if (isBackTime.equals(Preferences.SP_BACK_TIME)) {
                    calIntent.setTitle(getString(R.string.setting_back_to_the_clinic_time));
                    mPreferences.saveBackTime(calendar.getTimeInMillis());
                } else if (isBackTime.equals(Preferences.SP_TREATMENT_TIME)) {
                    calIntent.setTitle(getString(R.string.setting_treatment_time));
                    mPreferences.saveTreatmentTime(calendar.getTimeInMillis());
                }  else throw new IllegalArgumentException("Argument error");
                calIntent.setAllDay(true);
                Intent intent = calIntent.getIntentAfterSetting();
                startActivity(intent);
            }
        };
    }

    /************
     * Callback Func
     ***********/
    private SettingView.settingRepeatCallBack settingViewCallBack() {
        return new SettingView.settingRepeatCallBack() {
            @Override
            public void setLogout() {
                Preferences preferences = new Preferences(SettingActivity.this);
                preferences.clearAll();
                Intent intent = new Intent(SettingActivity.this, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SettingActivity.this.startActivity(intent);
                finish();
            }

            private static final String INTENT_TYPE = "plain/text";
            private final String[] ADDRESS = {"voice.dr.wang@gmail.com"};

            @Override
            public void setSendMail() {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType(INTENT_TYPE);
                sendIntent.putExtra(Intent.EXTRA_EMAIL, ADDRESS);
                startActivity(sendIntent);
            }

            @Override
            public void setDailyRepeat(boolean isChecked) {
                mPreferences.saveDailyRepeat(isChecked);
                showLog("Daily Switch", String.valueOf(isChecked));
                if (isChecked) {
                    setAlarmManagerOpen(AlarmConstantManager.MODE_DAILY);
                } else {
                    setAlarmManagerClose(AlarmConstantManager.MODE_DAILY);
                }
            }

            @Override
            public void setWeeklyRepeat(boolean isChecked) {
                mPreferences.saveWeeklyRepeat(isChecked);
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

            @Override
            public void setBackToTime(TextView yearText, TextView monthText, TextView dayText, TextView weekText) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SettingActivity.this
                        , 0
                        , onDateSetListener(Preferences.SP_BACK_TIME, yearText, monthText, dayText, weekText)
                        , year
                        , month
                        , day);
                datePickerDialog.show();
            }

            public void setTreatmentTime(final TextView yearText, final TextView monthText, final TextView dayText, final TextView weekText) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SettingActivity.this
                        , 0
                        , onDateSetListener(Preferences.SP_TREATMENT_TIME, yearText, monthText, dayText, weekText)
                        , year
                        , month
                        , day);
                datePickerDialog.show();
            }

            @Override
            public void onDoctorSetting() {
                ActivityLauncher.go(SettingActivity.this, DoctorSettingActivity.class, null);
                finish();
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