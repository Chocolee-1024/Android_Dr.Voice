package com.imac.voice_app.view.setting;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.component.ToolbarView;
import com.imac.voice_app.core.Ruler;
import com.imac.voice_app.module.Preferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    @BindView(R.id.doctor_setting)
    Button doctorSetting;

    @BindString(R.string.speak_speed_time_default)
    String mTimeDefaultText;
    @BindArray(R.array.week_array)
    String[] mWeekDayTextArray;

    private Context mContext;
    private Preferences mPreferences;

    public interface settingRepeatCallBack {
        void setLogout();

        void setSendMail();

        void setDailyRepeat(boolean isChecked);

        void setWeeklyRepeat(boolean isChecked);

        void setDailyAlarmTime(int id);

        void setWeeklyAlarmTime(int id);

        void setWeeklyAlarmDay();

        void setBackToTime(TextView yearText, TextView monthText, TextView dayText, TextView weekText);

        void setTreatmentTime(TextView yearText, TextView monthText, TextView dayText, TextView weekText);

        void onDoctorSetting();
    }

    private settingRepeatCallBack callBack = null;
    private Preferences initSetting;


    public SettingView(Activity activity, settingRepeatCallBack callBack, Preferences initSetting) {
        ButterKnife.bind(this, activity);
        mPreferences = new Preferences(activity);
        mContext = activity.getApplicationContext();
        this.callBack = callBack;
        this.initSetting = initSetting;
        initialSetting();


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
        setBackTime();
        setTreatmentTime();
        mDailyRepeatSwitch.setChecked(initSetting.getDailyRepeat());
        setDailyRepeatTextViewAlpha(initSetting.getDailyRepeat());
        mWeeklyRepeatSwitch.setChecked(initSetting.getWeeklyRepeat());
        setWeeklyRepeatTextViewAlpha(initSetting.getWeeklyRepeat());
        mWeeklyWeekTextView.setText(mWeekDayTextArray[initSetting.getWeeklyDay()]);
    }

    private void setBackTime() {
        long backTime = mPreferences.getBackTime();
        if (0==backTime)return;
        SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(backTime);
        int year= calendar.get(Calendar.YEAR);
        int month= calendar.get(Calendar.MONTH);
        int dayOfMonth= calendar.get(Calendar.DAY_OF_MONTH);
        String week = weekFormat.format(calendar.getTime());
        backToTheClinicTimeYear.setText(String.valueOf(year - 1911));
        backToTheClinicTimeMonth.setText(String.valueOf(month + 1));
        backToTheClinicTimeDay.setText(String.valueOf(dayOfMonth));
        backToTheClinicTimeWeek.setText(week);
    }

    private void setTreatmentTime() {
        long treatmentTime = mPreferences.getTreatmentTime();
        if (0==treatmentTime)return;
        SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(treatmentTime);
        int year= calendar.get(Calendar.YEAR);
        int month= calendar.get(Calendar.MONTH);
        int dayOfMonth= calendar.get(Calendar.DAY_OF_MONTH);
        String week = weekFormat.format(calendar.getTime());
        treatmentYear.setText(String.valueOf(year - 1911));
        treatmentMonth.setText(String.valueOf(month + 1));
        treatmentDay.setText(String.valueOf(dayOfMonth));
        treatmentWeek.setText(week);
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

    @OnClick(R.id.top_container)
    public void onBackTimeClick() {
        callBack.setBackToTime(backToTheClinicTimeYear, backToTheClinicTimeMonth, backToTheClinicTimeDay, backToTheClinicTimeWeek);
    }

    @OnClick(R.id.bottom_container)
    public void onTreatmentTimeClick() {
        callBack.setTreatmentTime(treatmentYear, treatmentMonth, treatmentDay, treatmentWeek);
    }
    @OnClick(R.id.doctor_setting)
    public void onDoctorSettingClick(){
        callBack.onDoctorSetting();
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
}