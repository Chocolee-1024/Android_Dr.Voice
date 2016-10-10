package com.imac.voice_app.module;

import android.app.Activity;
import android.util.Log;

import com.imac.voice_app.core.PreferencesHelper;
import com.imac.voice_app.util.weeklyassessment.WeeklyAssessmentActivity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by isa on 2016/10/8.
 */
public class DateChecker extends PreferencesHelper {
    private Activity activity;
    private final boolean DEBUG = true;

    public DateChecker(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public long getNextWeekFirstDayTime() {
        Calendar targetCal = Calendar.getInstance();
        targetCal.set(Calendar.HOUR_OF_DAY, 0);
        targetCal.clear(Calendar.MINUTE);
        targetCal.clear(Calendar.SECOND);
        targetCal.clear(Calendar.MILLISECOND);
        targetCal.set(Calendar.DAY_OF_WEEK, targetCal.getFirstDayOfWeek());
        targetCal.add(Calendar.WEEK_OF_YEAR, 1);
        Log.e("targetDate", targetCal.getTime() + "");
        return targetCal.getTimeInMillis();
    }

    public boolean isOverDay(Calendar currentCalendar) {
        if (DEBUG) {
            Log.e("currentCalendar", currentCalendar.getTime() + "");
            Date resultdate = new Date((long) get(WeeklyAssessmentActivity.KEY_WEEKLY_ENABLE_DATE, Type.LONG));
            Log.e("saveDate", "" + resultdate);
            Log.e("saveDate", currentCalendar.getTimeInMillis() - (long) get(WeeklyAssessmentActivity.KEY_WEEKLY_ENABLE_DATE, Type.LONG)+"");
        }
        if (0 != (long) get(WeeklyAssessmentActivity.KEY_WEEKLY_ENABLE_DATE, Type.LONG)) {
            return currentCalendar.getTimeInMillis() - (long) get(WeeklyAssessmentActivity.KEY_WEEKLY_ENABLE_DATE, Type.LONG) >= 0;
        }

        return false;
    }

    @Override
    public String getClassName() {
        return activity.getPackageName();
    }
}
