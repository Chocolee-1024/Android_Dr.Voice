package com.imac.dr.voice_app.view.history;

import android.app.Activity;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.component.Histogram;
import com.imac.dr.voice_app.module.DataAppend;
import com.imac.dr.voice_app.module.Preferences;
import com.imac.dr.voice_app.module.database.data.WeeklyDataStructure;
import com.imac.dr.voice_app.view.history.base.BasePagerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;

/**
 * Created by isa on 2016/10/24.
 */
public class WeeklyPagerAdapter extends BasePagerAdapter {
    @BindView(R.id.chart)
    Histogram chart;
    private Activity activity = null;
    private WeeklyDataStructure[] mWeeklyDataStructures = null;
    private String[] month = null;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat dayFormat;
    private SimpleDateFormat monthFormat;
    private int pointAddResult = 0;
    private Preferences preferences;

    public WeeklyPagerAdapter(Activity activity, String[] month, WeeklyDataStructure[] weeklyDataStructures) {
        super(activity, month, weeklyDataStructures);
        this.activity = activity;
        this.mWeeklyDataStructures = weeklyDataStructures;
        this.month = month;

        preferences = new Preferences(activity);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        dayFormat = new SimpleDateFormat("MM/dd", Locale.getDefault());
        monthFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
    }

    @Override
    protected String[] dateCompare(int position) {
        ArrayList<String> content = new ArrayList<>();
        String[] result;
        for (int index = 0; index < mWeeklyDataStructures.length; index++) {
            Date dayDate = null;
            Date monthDate = null;
            try {
                dayDate = dateFormat.parse(mWeeklyDataStructures[index].getDate());
                monthDate = monthFormat.parse(month[position]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar dayCal = Calendar.getInstance();
            Calendar monthCal = Calendar.getInstance();
            dayCal.setTime(dayDate);
            monthCal.setTime(monthDate);
            int subYear = dayCal.get(Calendar.YEAR) - monthCal.get(Calendar.YEAR);
            if (subYear == 0) {
                if (dayCal.get(Calendar.MONTH) == monthCal.get(Calendar.MONTH)) {
                    content.add(dayFormat.format(dayDate));
                }
            }
        }
        result = content.toArray(new String[0]);
        return result;
    }

    @Override
    protected PointStruction calculate(int position) {
        float result[] = null;
        String[] totalScoreResult = null;
        ArrayList<Float> content = new ArrayList<>();
        ArrayList<Integer> pointAddContent = new ArrayList<>();
        for (int index = 0; index < mWeeklyDataStructures.length; index++) {
            Date dayDate = null;
            Date monthDate = null;
            try {
                dayDate = dateFormat.parse(mWeeklyDataStructures[index].getDate());
                monthDate = monthFormat.parse(month[position]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar dayCal = Calendar.getInstance();
            Calendar monthCal = Calendar.getInstance();
            dayCal.setTime(dayDate);
            monthCal.setTime(monthDate);
            int subYear = dayCal.get(Calendar.YEAR) - monthCal.get(Calendar.YEAR);
            if (subYear == 0) {
                if (dayCal.get(Calendar.MONTH) == monthCal.get(Calendar.MONTH)) {
                    DataAppend dataAppend = new DataAppend();
                    ArrayList<String> weeklyArray = dataAppend.formatString(mWeeklyDataStructures[index].getWeeklyTopicPoint());
                    pointAddResult = 0;
                    for (int i = 0; i < weeklyArray.size(); i++) {
                        pointAddResult += Integer.valueOf(weeklyArray.get(i));
                    }
                    pointAddContent.add(Math.round(((float)pointAddResult / 40) * 100));
                    content.add(((float) pointAddResult / 40));
                }
            }
        }
        result = new float[content.size()];
        totalScoreResult = new String[content.size()];
        for (int i = 0; i < content.size(); i++) {
            result[i] = content.get(i);
            totalScoreResult[i] = String.valueOf(pointAddContent.get(i));
            totalScoreResult[i] = totalScoreResult[i] + "%";
        }
        PointStruction struction = new PointStruction();
        struction.setPointPercent(result);
        struction.setTotalScore(totalScoreResult);
        return struction;
    }
}
