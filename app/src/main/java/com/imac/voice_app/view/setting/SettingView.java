package com.imac.voice_app.view.setting;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.component.ToolbarView;
import com.imac.voice_app.core.Ruler;
import com.imac.voice_app.module.AlarmPreferences;
import com.imac.voice_app.module.FontManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Setting Page View
 * Created by flowmaHuang on 2016/9/21.
 */
public class SettingView {
    @BindView(R.id.setting_toolbar_view)
    ToolbarView mToolbarView;
    @BindView(R.id.tv_daily_notice_title)
    TextView mDailyNoticeTitleTextView;
    @BindView(R.id.tv_daily_notice_time)
    TextView mDailyTimeTextView;
    @BindView(R.id.tv_daily_notice_repeat)
    TextView mDailyRepeatTextView;
    @BindView(R.id.tv_weekly_notice_title)
    TextView mWeeklyNoticeTitleTextView;
    @BindView(R.id.tv_weekly_notice_time)
    TextView mWeeklyTimeTextView;
    @BindView(R.id.tv_weekly_notice_week)
    TextView mWeeklyWeekTextView;
    @BindView(R.id.tv_weekly_notice_repeat)
    TextView mWeeklyRepeatTextView;
    @BindView(R.id.sw_daily_notice_switch)
    Switch mDailyRepeatSwitch;
    @BindView(R.id.sw_weekly_notice_switch)
    Switch mWeeklyRepeatSwitch;
    @BindView(R.id.iv_connection_us)
    ImageView mConnectionUsImageView;
    @BindView(R.id.setting_logout)
    Button mLogoutButton;
    @BindView(R.id.back_to_the_clinic_time_year)
    TextView backToTheClinicTimeYear;
    @BindView(R.id.back_to_the_clinic_time_month)
    TextView backToTheClinicTimeMonth;
    @BindView(R.id.back_to_the_clinic_time_day)
    TextView backToTheClinicTimeDay;
    @BindView(R.id.back_to_the_clinic_time_week)
    TextView backToTheClinicTimeWeek;
    @BindView(R.id.back_to_the_clinic_time_time)
    TextView backToTheClinicTimeTime;
    @BindView(R.id.back_to_the_clinic_time_number)
    TextView backToTheClinicTimeNumber;
    @BindView(R.id.treatment_year)
    TextView treatmentYear;
    @BindView(R.id.treatment_month)
    TextView treatmentMonth;
    @BindView(R.id.treatment_day)
    TextView treatmentDay;
    @BindView(R.id.treatment_week)
    TextView treatmentWeek;
    @BindView(R.id.treatment_time)
    TextView treatmentTime;
    @BindView(R.id.treatment_number)
    TextView treatmentNumber;


    @BindString(R.string.speak_speed_time_default)
    String mTimeDefaultText;
    @BindArray(R.array.week_array)
    String[] mWeekDayTextArray;


    private ArrayList<String> remindData;

    public interface settingRepeatCallBack {
        void setLogout();

        void setSendMail();

        void setDailyRepeat(boolean isChecked);

        void setWeeklyRepeat(boolean isChecked);

        void setDailyAlarmTime(int id);

        void setWeeklyAlarmTime(int id);

        void setWeeklyAlarmDay();
    }

    private settingRepeatCallBack callBack = null;
    private AlarmPreferences initSetting;


    public SettingView(Activity activity, settingRepeatCallBack callBack, AlarmPreferences initSetting) {
        ButterKnife.bind(this, activity);
        Context mContext = activity.getApplicationContext();
        this.callBack = callBack;
        this.initSetting = initSetting;
        initialSetting();

        FontManager.setFont(mContext, FontManager.MEDIUM,
                mDailyNoticeTitleTextView, mDailyRepeatTextView,
                mWeeklyNoticeTitleTextView, mWeeklyWeekTextView, mWeeklyRepeatTextView);

        Ruler ruler = new Ruler(activity);
        mDailyRepeatSwitch.setSwitchMinWidth(ruler.getW(12));
        mWeeklyRepeatSwitch.setSwitchMinWidth(ruler.getW(12));
    }

    //進入頁面後根據儲存值更新畫面上各元件呈現狀態
    private void initialSetting() {
        if (initSetting.getDailyHour().equals("") || initSetting.getDailyMin().equals("")) {
            setDailyTimeTextView(mTimeDefaultText);
        } else {
            setDailyTimeTextView(initSetting.getDailyHour() + " : " + initSetting.getDailyMin());
        }

        if (initSetting.getWeeklyHour().equals("") || initSetting.getWeeklyMin().equals("")) {
            setWeeklyTimeTextView(mTimeDefaultText);
        } else {
            setWeeklyTimeTextView(initSetting.getWeeklyHour() + " : " + initSetting.getWeeklyMin());
        }
        mDailyRepeatSwitch.setChecked(initSetting.getDailyRepeat());
        setDailyRepeatTextViewAlpha(initSetting.getDailyRepeat());
        mWeeklyRepeatSwitch.setChecked(initSetting.getWeeklyRepeat());
        setWeeklyRepeatTextViewAlpha(initSetting.getWeeklyRepeat());
        mWeeklyWeekTextView.setText(mWeekDayTextArray[initSetting.getWeeklyDay()]);
    }

    private void setDailyRepeatTextViewAlpha(boolean isChecked) {
        if (isChecked) {
            mDailyRepeatTextView.setAlpha(1f);
        } else {
            mDailyRepeatTextView.setAlpha(0.5f);
        }
    }

    private void setWeeklyRepeatTextViewAlpha(boolean isChecked) {
        if (isChecked) {
            mWeeklyRepeatTextView.setAlpha(1f);
        } else {
            mWeeklyRepeatTextView.setAlpha(0.5f);
        }
    }

    /************
     * Callback Func
     ***********/
    @OnCheckedChanged({R.id.sw_daily_notice_switch, R.id.sw_weekly_notice_switch})
    public void repeatSwitchCheckChangeListener(Switch v) {
        switch (v.getId()) {
            case R.id.sw_daily_notice_switch:
                callBack.setDailyRepeat(v.isChecked());
                setDailyRepeatTextViewAlpha(v.isChecked());
                break;
            case R.id.sw_weekly_notice_switch:
                callBack.setWeeklyRepeat(v.isChecked());
                setWeeklyRepeatTextViewAlpha(v.isChecked());
                break;
        }
    }

    @OnClick({R.id.setting_logout, R.id.iv_connection_us,
            R.id.tv_weekly_notice_week, R.id.tv_daily_notice_time,
            R.id.tv_weekly_notice_time})
    public void connectionClickListener(View v) {
        switch (v.getId()) {
            case R.id.setting_logout:
                callBack.setLogout();
                break;
            case R.id.iv_connection_us:
                callBack.setSendMail();
                break;
            case R.id.tv_weekly_notice_week:
                callBack.setWeeklyAlarmDay();
                break;
            case R.id.tv_daily_notice_time:
                callBack.setDailyAlarmTime(v.getId());
                break;
            case R.id.tv_weekly_notice_time:
                callBack.setWeeklyAlarmTime(v.getId());
                break;
        }
    }

    private void setTextParams() {
        String[] backToClinicArray = remindData.get(0).split("[,/]+");
        String[] treatmentArray = remindData.get(1).split("[,/]+");
        boolean isNone = true;
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        for (String index : backToClinicArray) {
            Matcher matcher = pattern.matcher(index);
            isNone &= matcher.find();
            if (isNone) return;
        }

        String backToClinicYearFormat = String.valueOf(Integer.valueOf(backToClinicArray[0]) - 1911);
        backToTheClinicTimeYear.setText(backToClinicYearFormat);
        backToTheClinicTimeMonth.setText(backToClinicArray[1]);
        backToTheClinicTimeDay.setText(backToClinicArray[2]);
        backToTheClinicTimeTime.setText(backToClinicArray[3]);
        backToTheClinicTimeWeek.setText(getCalendarWeek(remindData.get(0).split(",")[0]));
        backToTheClinicTimeNumber.setText(backToClinicArray[4]);
        isNone = true;
        for (String index : treatmentArray) {
            Matcher matcher = pattern.matcher(index);
            isNone &= matcher.find();
            if (isNone) return;
        }
        String treatmentYearFormat = String.valueOf(Integer.valueOf(treatmentArray[0]) - 1911);
        treatmentYear.setText(treatmentYearFormat);
        treatmentMonth.setText(treatmentArray[1]);
        treatmentDay.setText(treatmentArray[2]);
        treatmentTime.setText(treatmentArray[3]);
        treatmentWeek.setText(getCalendarWeek(remindData.get(1).split(",")[0]));
        treatmentNumber.setText(treatmentArray[4]);
    }

    private String getCalendarWeek(String date) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.CHINA);

        int year = Integer.valueOf(date.split("/")[0]);
        int month = Integer.valueOf(date.split("/")[1]);
        int day = Integer.valueOf(date.split("/")[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return dayFormat.format(calendar.getTime());
    }

    /************
     * public method
     ***********/
    public void setDailyTimeTextView(String time) {
        mDailyTimeTextView.setText(time);
    }

    public void setWeeklyTimeTextView(String time) {
        mWeeklyTimeTextView.setText(time);
    }

    public void setWeeklyWeekTextView(String day) {
        mWeeklyWeekTextView.setText(day);
    }

    public void setToolbarViewCallBack(ToolbarView.toolbarCallBack callBack) {
        mToolbarView.setToolbarButtonCallBack(callBack);
    }

    public void setRemindData(ArrayList<String> data) {
        this.remindData = data;
        Log.e("show", String.valueOf(data));
        setTextParams();
    }


}