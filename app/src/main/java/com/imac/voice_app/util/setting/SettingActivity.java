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
import android.view.View;
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
import com.imac.voice_app.util.doctorsetting.DoctorSettingActivity;
import com.imac.voice_app.util.homepage.HomePageActivity;
import com.imac.voice_app.view.setting.SettingView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static com.imac.voice_app.module.AlarmConstantManager.ID_DAILY_ONE;
import static com.imac.voice_app.module.AlarmConstantManager.ID_DAILY_THREE;
import static com.imac.voice_app.module.AlarmConstantManager.ID_DAILY_TWO;
import static com.imac.voice_app.module.AlarmConstantManager.ID_WEEKLY_ALARM;
import static com.imac.voice_app.module.AlarmConstantManager.MODE_WEEK;


/**
 * Setting page activity Created by flowmaHuang on 2016/9/21.
 */
public class SettingActivity extends Activity {
    private SettingView mSettingLayout;
    private AlertDialog mWeekPickDialog;
    private Preferences mPreferences;
    private static boolean logOpen = true;
    private static final String INTENT_TYPE = "plain/text";
    private final String[] ADDRESS = {"voice.dr.wang@gmail.com"};
    private AlarmManager mAlarmManager;
    private final String BORADCASTFILTER = "com.imac.voice_app.broadcastreceiver.AlarmReceiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        firstUseSetting();
    }

    private void init() {
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
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

    private void setTime(final int id) {
        GregorianCalendar calendar = new GregorianCalendar();
        TimePickerDialog.OnTimeSetListener listener;
        listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                long millis = calendar.getTimeInMillis();

                String hour = (hourOfDay >= 10) ? String.valueOf(hourOfDay) : "0" + String.valueOf(hourOfDay);
                String min = (minute >= 10) ? String.valueOf(minute) : "0" + String.valueOf(minute);
                String time = hour + " : " + min;
                if (id == R.id.daily_text1) {
                    mSettingLayout.setDailyTimeTextViewOne(time);
                    mPreferences.saveDailyTimeOne(hour, min);
                    mPreferences.saveDailyTimeOneMillis(millis);
                    if (mPreferences.getDailyOneRepeat()) {
                        setNotifyDate(AlarmConstantManager.MODE_DAILY_ONE, millis, AlarmConstantManager.ID_DAILY_ONE);
                    }
                } else if (id == R.id.daily_text2) {
                    mSettingLayout.setDailyTimeTextViewTwo(time);
                    mPreferences.saveDailyTimeTwo(hour, min);
                    mPreferences.saveDailyTimeTwoMillis(millis);

                    if (mPreferences.getDailyTwoRepeat()) {
                        setNotifyDate(AlarmConstantManager.MODE_DAILY_TWO, millis, AlarmConstantManager.ID_DAILY_TWO);
                    }
                } else if (id == R.id.daily_text3) {
                    mSettingLayout.setDailyTimeTextViewThree(time);
                    mPreferences.saveDailyTimeThree(hour, min);
                    mPreferences.saveDailyTimeThreeMillis(millis);

                    if (mPreferences.getDailyThreeRepeat()) {
                        setNotifyDate(AlarmConstantManager.MODE_DAILY_THREE, millis, AlarmConstantManager.ID_DAILY_THREE);
                    }
                } else {
                    mSettingLayout.setWeeklyTimeTextView(time);
                    mPreferences.saveWeeklyTime(hour, min);
                    mPreferences.saveWeeklyMillis(millis);

                    if (mPreferences.getWeeklyRepeat()) {
                        setNotifyWeekly(MODE_WEEK, millis, ID_WEEKLY_ALARM);
                    }
                }
                showLog("Time Picker", "Daily" + "/" + hour + ":" + min);
            }
        };
        TimePickerDialog mTimePickerDialog = new TimePickerDialog(SettingActivity.this, listener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        mTimePickerDialog.show();
    }

    private AlertDialog getMultiItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.week_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String day = (String) getResources().getTextArray(R.array.week_array)[which];
                mSettingLayout.setWeeklyWeekTextView(day);
                mPreferences.saveWeeklyDay(which);
                setNotifyWeekly(MODE_WEEK, mPreferences.getWeeklyMillis(), ID_WEEKLY_ALARM);
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

    private void setNotifyDate(String mode, long notifyDateTime, int id) {
        Intent intent = new Intent(SettingActivity.this, AlarmReceiver.class);
        intent.putExtra(AlarmConstantManager.INTENT_MODE, mode);
        PendingIntent backAlarmIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (null != backAlarmIntent) backAlarmIntent.cancel();
        backAlarmIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notifyDateTime, 24 * 60 * 60 * 1000, backAlarmIntent);
        Log.d("tag", "setNotifyDate ");
    }

    private void setNotifyWeekly(String mode, long notifyDateTime, int id) {
        Intent intent = new Intent(SettingActivity.this, AlarmReceiver.class);
        PendingIntent backAlarmIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (null != backAlarmIntent) backAlarmIntent.cancel();
        Bundle bundle = new Bundle();
        bundle.putString(AlarmConstantManager.INTENT_MODE, mode);
        bundle.putString(AlarmConstantManager.WEEK_DAY, String.valueOf(mPreferences.getWeeklyDay()));
        intent.putExtras(bundle);
        backAlarmIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notifyDateTime, 24 * 60 * 60 * 1000, backAlarmIntent);
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener(final String isBackTime,
                                                                 final TextView yearText, final TextView monthText, final TextView dayText,
                                                                 final TextView weekText, final TextView treatmentText) {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE", Locale.TAIWAN);
                CalendarIntentHelper calIntent = new CalendarIntentHelper();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String week = weekFormat.format(calendar.getTime());

                yearText.setText(String.valueOf(year - 1911));
                monthText.setText(String.valueOf(month + 1));
                dayText.setText(String.valueOf(dayOfMonth));
                weekText.setText(week);

                if (isBackTime.equals(Preferences.SP_TREATMENT_TIME)) {
                    new TimePickerDialog(SettingActivity.this
                            , onTimeSetListener(treatmentText, year, month, dayOfMonth)
                            , calendar.get(Calendar.HOUR)
                            , calendar.get(Calendar.MINUTE)
                            , true
                    ).show();
                } else if (isBackTime.equals(Preferences.SP_BACK_TIME)) {
                    calIntent.setTitle(getString(R.string.setting_time));
                    mPreferences.saveBackTime(calendar.getTimeInMillis());
                    calIntent.setAllDay(true);
                    calIntent.setBeginTimeInMillis(calendar.getTimeInMillis());
                    calIntent.setEndTimeInMillis(calendar.getTimeInMillis());
                    calIntent.setDescription(mPreferences.getBackNumber() + "號");
                    Intent intent = calIntent.getIntentAfterSetting();
                    startActivity(intent);
                }
            }
        };
    }

    private TimePickerDialog.OnTimeSetListener onTimeSetListener(final TextView treatmentText, final int year, final int month, final int dayOfMonth) {
        return new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (null != treatmentText) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    mPreferences.saveTreatmentTime(calendar.getTimeInMillis());
                    treatmentText.setText(hourOfDay + ":" + (minute > 9 ? minute : "0" + minute));

                    CalendarIntentHelper calIntent = new CalendarIntentHelper();
                    calIntent.setTitle(getString(R.string.setting_time));
                    mPreferences.saveBackTime(calendar.getTimeInMillis());
                    calIntent.setAllDay(false);
                    calIntent.setBeginTimeInMillis(calendar.getTimeInMillis());
                    calIntent.setEndTimeInMillis(calendar.getTimeInMillis());
                    Intent intent = calIntent.getIntentAfterSetting();
                    startActivity(intent);
                }
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
            }

            @Override
            public void setWeeklyRepeat(boolean isChecked) {
                mPreferences.saveWeeklyRepeat(isChecked);
                if (!isChecked) {
                    Intent intent = new Intent(SettingActivity.this, AlarmReceiver.class);
                    intent.putExtra(AlarmConstantManager.INTENT_MODE, AlarmConstantManager.MODE_WEEK);
                    intent.putExtra(AlarmConstantManager.WEEK_DAY, String.valueOf(mPreferences.getWeeklyDay()));
                    PendingIntent backAlarmIntent = PendingIntent.getBroadcast(SettingActivity.this, AlarmConstantManager.ID_WEEKLY_ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mAlarmManager.cancel(backAlarmIntent);
                    Log.d("tag", "cancel ");
                } else {
                    setNotifyWeekly(MODE_WEEK, mPreferences.getWeeklyMillis(), ID_WEEKLY_ALARM);
                }
            }

            @Override
            public void setDailyOneRepeat(boolean isChecked) {
                mPreferences.saveDailyOneRepeat(isChecked);
                if (!isChecked) {
                    Intent intent = new Intent(SettingActivity.this, AlarmReceiver.class);
                    intent.putExtra(AlarmConstantManager.INTENT_MODE, AlarmConstantManager.MODE_DAILY_ONE);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingActivity.this, AlarmConstantManager.ID_DAILY_ONE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mAlarmManager.cancel(pendingIntent);
                    Log.d("tag", "cancel ");
                } else {
                    setNotifyDate(AlarmConstantManager.MODE_DAILY_ONE, mPreferences.getDailyTimeOneMillis(), ID_DAILY_ONE);
                }
            }

            @Override
            public void setDailyTwoRepeat(boolean isChecked) {
                mPreferences.saveDailyTwoRepeat(isChecked);
                if (!isChecked) {
                    Intent intent = new Intent(SettingActivity.this, AlarmReceiver.class);
                    intent.putExtra(AlarmConstantManager.INTENT_MODE, AlarmConstantManager.MODE_DAILY_TWO);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingActivity.this, ID_DAILY_TWO, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mAlarmManager.cancel(pendingIntent);
                    Log.d("tag", "cancel ");
                } else {
                    setNotifyDate(AlarmConstantManager.MODE_DAILY_TWO, mPreferences.getDailyTimeTwoMillis(), ID_DAILY_TWO);
                }
            }

            @Override
            public void setDailyThreeRepeat(boolean isChecked) {
                mPreferences.saveDailyThreeRepeat(isChecked);
                if (!isChecked) {
                    Intent intent = new Intent(SettingActivity.this, AlarmReceiver.class);
                    intent.putExtra(AlarmConstantManager.INTENT_MODE, AlarmConstantManager.MODE_DAILY_THREE);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingActivity.this, AlarmConstantManager.ID_DAILY_THREE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mAlarmManager.cancel(pendingIntent);
                    Log.d("tag", "cancel ");
                } else {
                    setNotifyDate(AlarmConstantManager.MODE_DAILY_THREE, mPreferences.getDailyTimeThreeMillis(), ID_DAILY_THREE);
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
                        , onDateSetListener(Preferences.SP_BACK_TIME, yearText, monthText, dayText, weekText, null)
                        , year
                        , month
                        , day);
                datePickerDialog.show();
            }

            @Override
            public void setTreatmentTime(final TextView yearText, final TextView monthText, final TextView dayText, final TextView weekText, TextView treatmentText) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SettingActivity.this
                        , 0
                        , onDateSetListener(Preferences.SP_TREATMENT_TIME, yearText, monthText, dayText, weekText, treatmentText)
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

            @Override
            public void dailyClick(View dailyExerciseDialog) {

            }

            @Override
            public void onDailyOneClick(int id) {
                setTime(id);
            }

            @Override
            public void onDailyTwoClick(int id) {
                setTime(id);
            }

            @Override
            public void onDailyThreeClick(int id) {
                setTime(id);
            }

            @Override
            public void onNumTextChange(String text) {
                mPreferences.saveBackNumber(text);
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