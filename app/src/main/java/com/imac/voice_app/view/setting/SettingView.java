package com.imac.voice_app.view.setting;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import butterknife.OnTextChanged;

import static com.imac.voice_app.R.id.back_to_the_clinic_time_number;

/**
 * Setting Page View Created by flowmaHuang on 2016/9/21.
 */
public class SettingView {
    @BindView(R.id.setting_toolbar_view)
    ToolbarView mToolbarView;
    //    @BindView(R.id.tv_daily_notice_title)
//    TextView mDailyNoticeTitleTextView;
//    @BindView(R.id.tv_daily_notice_time)
//    TextView mDailyTimeTextView;
//    @BindView(R.id.tv_daily_notice_repeat)
//    TextView mDailyRepeatTextView;
    @BindView(R.id.tv_weekly_notice_title)
    TextView mWeeklyNoticeTitleTextView;
    @BindView(R.id.tv_weekly_notice_time)
    TextView mWeeklyTimeTextView;
    @BindView(R.id.tv_weekly_notice_week)
    TextView mWeeklyWeekTextView;
    @BindView(R.id.tv_weekly_notice_repeat)
    TextView mWeeklyRepeatTextView;
    //    @BindView(R.id.sw_daily_notice_switch)
//    Switch mDailyRepeatSwitch;
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
    @BindView(back_to_the_clinic_time_number)
    EditText backToTheClinicTimeNumber;
    @BindView(R.id.treatment_year)
    TextView treatmentYear;
    @BindView(R.id.treatment_month)
    TextView treatmentMonth;
    @BindView(R.id.treatment_day)
    TextView treatmentDay;
    @BindView(R.id.treatment_week)
    TextView treatmentWeek;
    //    @BindView(R.id.treatment_time)
//    TextView treatmentTime;
    @BindView(R.id.treatment_number)
    TextView treatmentNumber;
    @BindView(R.id.doctor_setting)
    Button doctorSetting;
    @BindView(R.id.daily_exercise_remind)
    TextView dailyExerciseRemind;
    @BindView(R.id.dialog_container)
    View dailyExerciseDialog;
    @BindView(R.id.close_button)
    ImageView closeButton;
    @BindView(R.id.daily_text1)
    TextView mDailyText1;
    @BindView(R.id.daily_switch1)
    Switch mDailySwitch1;
    @BindView(R.id.daily_text2)
    TextView mDailyText2;
    @BindView(R.id.daily_switch2)
    Switch mDailySwitch2;
    @BindView(R.id.daily_text3)
    TextView mDailyText3;
    @BindView(R.id.daily_switch3)
    Switch mDailySwitch3;

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

        void setDailyOneRepeat(boolean isChecked);

        void setDailyTwoRepeat(boolean isChecked);

        void setDailyThreeRepeat(boolean isChecked);

        void setDailyAlarmTime(int id);

        void setWeeklyAlarmTime(int id);

        void setWeeklyAlarmDay();

        void setBackToTime(TextView yearText, TextView monthText, TextView dayText, TextView weekText);

        void setTreatmentTime(TextView yearText, TextView monthText, TextView dayText, TextView weekText, TextView treatmentText);

        void onDoctorSetting();

        void dailyClick(View dailyExerciseDialogContainer);

        void onDailyOneClick(int id);

        void onDailyTwoClick(int id);

        void onDailyThreeClick(int id);

        void onNumTextChange(String text);
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
        mDailySwitch1.setSwitchMinWidth(ruler.getW(12));
        mDailySwitch2.setSwitchMinWidth(ruler.getW(12));
        mDailySwitch3.setSwitchMinWidth(ruler.getW(12));
        mWeeklyRepeatSwitch.setSwitchMinWidth(ruler.getW(12));
    }

    //進入頁面後根據儲存值更新畫面上各元件呈現狀態
    private void initialSetting() {
        setBackTime();
        setTreatmentTime();
        mDailySwitch1.setChecked(initSetting.getDailyOneRepeat());
        mDailySwitch2.setChecked(initSetting.getDailyTwoRepeat());
        mDailySwitch3.setChecked(initSetting.getDailyThreeRepeat());
        setTextContent(mWeeklyTimeTextView, initSetting.getWeeklyHour(), initSetting.getWeeklyMin());
        setTextContent(mDailyText1, initSetting.getDailyOneHour(), initSetting.getDailyOneMin());
        setTextContent(mDailyText2, initSetting.getDailyTwoHour(), initSetting.getDailyTwoMin());
        setTextContent(mDailyText3, initSetting.getDailyThreeHour(), initSetting.getDailyThreeMin());
        setRepeatTextViewAlpha(mDailyText1, initSetting.getDailyOneRepeat());
        setRepeatTextViewAlpha(mDailyText2, initSetting.getDailyTwoRepeat());
        setRepeatTextViewAlpha(mDailyText3, initSetting.getDailyThreeRepeat());
        setRepeatTextViewAlpha(mWeeklyRepeatTextView, initSetting.getWeeklyRepeat());
        mWeeklyRepeatSwitch.setChecked(initSetting.getWeeklyRepeat());
        mWeeklyWeekTextView.setText(mWeekDayTextArray[initSetting.getWeeklyDay()]);
        backToTheClinicTimeNumber.setText(initSetting.getBackNumber());

    }

    private void setTextContent(TextView textView, String str, String str2) {
        if ("".equals(str) || "".equals(str2)) textView.setText(mTimeDefaultText);
        else textView.setText(str + " : " + str2);
    }

    private void setBackTime() {
        long backTime = mPreferences.getBackTime();
        if (0 == backTime) return;
        SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(backTime);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String week = weekFormat.format(calendar.getTime());
        backToTheClinicTimeYear.setText(String.valueOf(year - 1911));
        backToTheClinicTimeMonth.setText(String.valueOf(month + 1));
        backToTheClinicTimeDay.setText(String.valueOf(dayOfMonth));
        backToTheClinicTimeWeek.setText(week);
    }

    private void setTreatmentTime() {
        long treatmentTime = mPreferences.getTreatmentTime();
        if (0 == treatmentTime) return;
        SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(treatmentTime);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String week = weekFormat.format(calendar.getTime());
        treatmentYear.setText(String.valueOf(year - 1911));
        treatmentMonth.setText(String.valueOf(month + 1));
        treatmentDay.setText(String.valueOf(dayOfMonth));
        treatmentWeek.setText(week);
        treatmentNumber.setText(hourOfDay + ":" + (minute > 9 ? minute : "0" + minute));
    }

    private void setRepeatTextViewAlpha(TextView textView, boolean isChecked) {
        textView.setClickable(isChecked);
        if (isChecked) {
            textView.setAlpha(1f);
        } else {
            textView.setAlpha(0.5f);
        }
    }

    /************
     * Callback Func
     ***********/
    @OnCheckedChanged({R.id.daily_switch1, R.id.daily_switch2, R.id.daily_switch3, R.id.sw_weekly_notice_switch})
    public void repeatSwitchCheckChangeListener(Switch v) {
        switch (v.getId()) {
            case R.id.daily_switch1:
                callBack.setDailyOneRepeat(v.isChecked());
                setRepeatTextViewAlpha(mDailyText1, v.isChecked());
                break;
            case R.id.daily_switch2:
                callBack.setDailyTwoRepeat(v.isChecked());
                setRepeatTextViewAlpha(mDailyText2, v.isChecked());
                break;
            case R.id.daily_switch3:
                callBack.setDailyThreeRepeat(v.isChecked());
                setRepeatTextViewAlpha(mDailyText3, v.isChecked());

                break;
            case R.id.sw_weekly_notice_switch:
                callBack.setWeeklyRepeat(v.isChecked());
                setRepeatTextViewAlpha(mWeeklyRepeatTextView, v.isChecked());
                break;
        }
    }

    @OnTextChanged(back_to_the_clinic_time_number)
    public void onTextNumChange(CharSequence text) {
        callBack.onNumTextChange(text.toString());
    }

    @OnClick({R.id.setting_logout, R.id.iv_connection_us,
            R.id.tv_weekly_notice_week, R.id.daily_text1,
            R.id.daily_text2, R.id.daily_text3, R.id.tv_weekly_notice_time})
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
            case R.id.tv_weekly_notice_time:
                callBack.setWeeklyAlarmTime(v.getId());
                break;
            case R.id.daily_text1:
                callBack.onDailyOneClick(v.getId());
                break;
            case R.id.daily_text2:
                callBack.onDailyTwoClick(v.getId());
                break;
            case R.id.daily_text3:
                callBack.onDailyThreeClick(v.getId());
                break;
        }
    }

    @OnClick(R.id.top_container)
    public void onBackTimeClick() {
        callBack.setBackToTime(backToTheClinicTimeYear, backToTheClinicTimeMonth, backToTheClinicTimeDay, backToTheClinicTimeWeek);
    }

    @OnClick(R.id.bottom_container)
    public void onTreatmentTimeClick() {
        callBack.setTreatmentTime(treatmentYear, treatmentMonth, treatmentDay, treatmentWeek, treatmentNumber);
    }

    @OnClick(R.id.doctor_setting)
    public void onDoctorSettingClick() {
        callBack.onDoctorSetting();
    }

    @OnClick(R.id.daily_exercise_remind)
    public void onDailyRemindClick() {
        dailyExerciseDialog.setVisibility(View.VISIBLE);
        callBack.dailyClick(dailyExerciseDialog);
    }

    @OnClick(R.id.close_button)
    public void onCLoseButtonClick() {
        dailyExerciseDialog.setVisibility(View.GONE);
    }

    /************
     * public method
     ***********/
    public void setWeeklyTimeTextView(String time) {
        mWeeklyTimeTextView.setText(time);
    }

    public void setWeeklyWeekTextView(String day) {
        mWeeklyWeekTextView.setText(day);
    }

    public void setToolbarViewCallBack(ToolbarView.toolbarCallBack callBack) {
        mToolbarView.setToolbarButtonCallBack(callBack);
    }

    public void setDailyTimeTextViewOne(String time) {
        mDailyText1.setText(time);
    }

    public void setDailyTimeTextViewTwo(String time) {
        mDailyText2.setText(time);
    }

    public void setDailyTimeTextViewThree(String time) {
        mDailyText3.setText(time);
    }
}