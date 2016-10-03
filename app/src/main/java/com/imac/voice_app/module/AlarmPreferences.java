package com.imac.voice_app.module;

import android.content.Context;

import com.imac.voice_app.core.PreferencesHelper;

/**
 * SharePreferences for Setting Page Alarm
 * Created by flowmaHuang on 2016/9/29.
 */
public class AlarmPreferences extends PreferencesHelper {
    private final String SP_FiLE_NAME = AlarmPreferences.class.getName();
    private final String SP_DAILY_IS_REPEAT = "SP_DAILY_ISREPEAT";
    private final String SP_DAILY_HOUR = "SP_DAILY_HOUR";
    private final String SP_DAILY_MIN = "SP_DAILY_MIN";
    private final String SP_WEEKLY_IS_REPEAT = "SP_WEEKLY_ISREPEAT";
    private final String SP_WEEKLY_DAY = "SP_WEEKLY_DAY";
    private final String SP_WEEKLY_HOUR = "SP_WEEKLY_HOUR";
    private final String SP_WEEKLY_MIN = "SP_WEEKLY_MIN";

    public AlarmPreferences(Context context) {
        super(context);
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

    @Override
    public String getClassName() {
        return SP_FiLE_NAME;
    }
}
