package com.imac.voice_app.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.imac.voice_app.core.PreferencesHelper;

/**
 * SharePreferences for Setting Page Alarm
 * Created by flowmaHuang on 2016/9/29.
 */
public class Preferences extends PreferencesHelper {
    private final String SP_FiLE_NAME = Preferences.class.getName();
    private final String SP_DAILY_IS_REPEAT = "SP_DAILY_ISREPEAT";
    private final String SP_DAILY_HOUR = "SP_DAILY_HOUR";
    private final String SP_DAILY_MIN = "SP_DAILY_MIN";
    private final String SP_WEEKLY_IS_REPEAT = "SP_WEEKLY_ISREPEAT";
    private final String SP_WEEKLY_DAY = "SP_WEEKLY_DAY";
    private final String SP_WEEKLY_HOUR = "SP_WEEKLY_HOUR";
    private final String SP_WEEKLY_MIN = "SP_WEEKLY_MIN";
    private final static String NAME_SHAREPREFERENCE = "name_share_preference";
    private final static String KEY_LOGIN_ACCOUNT = "key_login_account";
    private final static String KEY_LOGIN_NAME = "key_login_name";
    private final static String KEY_DAILY_EXERCISE = "key_daily_exercise";
    private final static String KEY_WEEKLY_EXERCISE = "key_weekly_exercise";
    private final static String KEY_SETTING = "key_setting";
    private static final String KEY_STATUS = "key_status";
    private static final String KEY_COMPLETE = "key_complete";
    private static final String KEY_SOUND_TOPIC = "key_sound_topic";
    private static final String KEY_WEEKLY_ENABLE_DATE = "key_weekly_enable_date";
    private static final String KEY_DAILY_COMPLETE = "key_daily_complete";
    private static final String KEY_DAILY_ENABLE_DATE = "key_daily_enable_date";
    private static final String KEY_DOCTOR_DAILY_SETTING = "key_doctor_daily_setting";
    private static final String KEY_DOCTOR_WEEKLY_SETTING = "key_doctor_weekly_setting";
    private static final String KEY_DOCTOR_SPEED_SETTING = "key_doctor_speed_setting";

    public static final String SP_BACK_TIME = "sp_back_time";
    public static final String SP_TREATMENT_TIME = "sp_treatment_time";

    public Preferences(Context context) {
        super(context);
    }

    public void saveAccount(String account) {
        save(Type.STRING, KEY_LOGIN_ACCOUNT, account);
    }

    public void saveName(String name) {
        save(Type.STRING, KEY_LOGIN_NAME, name);
    }

    public void saveComplete(boolean status) {
        save(Type.BOOLEAN, KEY_COMPLETE, status);
    }

    public void saveWeeklyEnableDate(long weeklyEnableDate) {
        save(Type.LONG, KEY_WEEKLY_ENABLE_DATE, weeklyEnableDate);
    }


    public void saveDailyTime(String hour, String min) {
        save(Type.STRING, SP_DAILY_HOUR, hour);
        save(Type.STRING, SP_DAILY_MIN, min);
    }

    public void saveWeeklyTime(String hour, String min) {
        save(Type.STRING, SP_WEEKLY_HOUR, hour);
        save(Type.STRING, SP_WEEKLY_MIN, min);
    }

    public void saveWeeklyDay(int day) {
        save(Type.INT, SP_WEEKLY_DAY, day);
    }

    public void saveDailyRepeat(boolean isRepeated) {
        save(Type.BOOLEAN, SP_DAILY_IS_REPEAT, isRepeated);
    }

    public void saveWeeklyRepeat(boolean isRepeated) {
        save(Type.BOOLEAN, SP_WEEKLY_IS_REPEAT, isRepeated);
    }

    public void saveBackTime(long backTime) {
        save(Type.LONG, SP_BACK_TIME, backTime);
    }

    public void saveTreatmentTime(long treatmentTime) {
        save(Type.LONG, SP_TREATMENT_TIME, treatmentTime);
    }

    public void saveDailyDoctorSetting(String value) {
        save(Type.STRING, KEY_DOCTOR_DAILY_SETTING, value);
    }

    public void saveWeeklyDoctorSetting(String value) {
        save(Type.STRING, KEY_DOCTOR_WEEKLY_SETTING, value);
    }

    public void saveSpeedDoctorSetting(boolean value) {
        save(Type.BOOLEAN, KEY_DOCTOR_SPEED_SETTING, value);
    }

    public String getAccounnt() {
        return get(KEY_LOGIN_ACCOUNT, Type.STRING).toString();
    }

    public String getName() {
        return get(KEY_LOGIN_NAME, Type.STRING).toString();
    }

    public boolean getComplete() {
        return (boolean) get(KEY_COMPLETE, Type.BOOLEAN);
    }

    public long getWeeklyEnableDate() {
        return (long) get(KEY_WEEKLY_ENABLE_DATE, Type.LONG);
    }

    public long getDailyEnableDate() {
        return (long) get(KEY_DAILY_ENABLE_DATE, Type.LONG);
    }


    public String getDailyHour() {
        return get(SP_DAILY_HOUR, Type.STRING).toString();
    }

    public String getDailyMin() {
        return get(SP_DAILY_MIN, Type.STRING).toString();
    }

    public int getWeeklyDay() {
        return (int) get(SP_WEEKLY_DAY, Type.INT);
    }

    public String getWeeklyHour() {
        return get(SP_WEEKLY_HOUR, Type.STRING).toString();
    }

    public String getWeeklyMin() {
        return get(SP_WEEKLY_MIN, Type.STRING).toString();
    }

    public Boolean getDailyRepeat() {
        return (Boolean) get(SP_DAILY_IS_REPEAT, Type.BOOLEAN);
    }

    public Boolean getWeeklyRepeat() {
        return (Boolean) get(SP_WEEKLY_IS_REPEAT, Type.BOOLEAN);
    }

    public long getBackTime() {
        return (long) get(SP_BACK_TIME, Type.LONG);
    }

    public long getTreatmentTime() {
        return (long) get(SP_TREATMENT_TIME, Type.LONG);
    }

    public String getDailyDoctorSetting() {
        return (String) get(KEY_DOCTOR_DAILY_SETTING, Type.STRING);
    }

    public String getWeeklyDoctorSetting() {
        return (String) get(KEY_DOCTOR_WEEKLY_SETTING, Type.STRING);
    }

    public boolean getSpeedDoctorSetting() {
        return (boolean) get(KEY_DOCTOR_SPEED_SETTING, Type.BOOLEAN);
    }

    public void clearAll() {
        SharedPreferences store = getContext().getSharedPreferences(getClassName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = store.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public String getClassName() {
        return SP_FiLE_NAME;
    }
}
