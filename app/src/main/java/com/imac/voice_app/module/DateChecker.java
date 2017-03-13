package com.imac.voice_app.module;

import android.app.Activity;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by isa on 2016/10/8.
 */
public class DateChecker {
    private Activity activity;
    private final boolean DEBUG = true;
    private Preferences preferences;

    public DateChecker(Activity activity) {
        this.activity = activity;
        preferences = new Preferences(activity);
    }

    public long getNextWeekFirstDayTime() {
        Calendar targetCal = Calendar.getInstance();
        targetCal.set(Calendar.HOUR_OF_DAY, 0);
        targetCal.clear(Calendar.MINUTE);
        targetCal.clear(Calendar.SECOND);
        targetCal.clear(Calendar.MILLISECOND);
        targetCal.set(Calendar.DAY_OF_WEEK, targetCal.getFirstDayOfWeek());
        targetCal.add(Calendar.WEEK_OF_YEAR, 1);
        Log.e("getNextWeekFirstDayTime", targetCal.getTime() + "");
        return targetCal.getTimeInMillis();
    }

    public boolean isOverWeek(Calendar currentCalendar) {
        long saveDate = preferences.getWeeklyEnableDate();
        if (DEBUG) {
            Log.e("currentCalendar", currentCalendar.getTime() + "");
            Date resultdate = new Date(saveDate);
            Log.e("saveDate", "" + resultdate);
            Log.e("saveDate", currentCalendar.getTimeInMillis() - saveDate + "");
        }
        if (0 != saveDate) {
            return currentCalendar.getTimeInMillis() - saveDate >= 0;
        }
        return false;
    }

    public long getNextDayFirstTime() {
        Calendar targetCal = Calendar.getInstance();
        targetCal.set(Calendar.HOUR_OF_DAY, 0);
        targetCal.clear(Calendar.MINUTE);
        targetCal.clear(Calendar.SECOND);
        targetCal.clear(Calendar.MILLISECOND);
        targetCal.add(Calendar.DATE, 1);
        Log.e("getNextDayFirstTime", targetCal.getTime() + "");
        return targetCal.getTimeInMillis();
    }

    public boolean isOverDay(Calendar currentCalendar) {
        long saveDate = preferences.getDailyEnableDate();
        if (DEBUG) {
            Log.e("currentCalendar", currentCalendar.getTime() + "");
            Date resultdate = new Date(saveDate);
            Log.e("saveDate", "" + resultdate);
            Log.e("saveDate", currentCalendar.getTimeInMillis() - saveDate + "");
        }
        if (0 != saveDate) {
            return currentCalendar.getTimeInMillis() - saveDate >= 0;
        }
        return false;
    }
}