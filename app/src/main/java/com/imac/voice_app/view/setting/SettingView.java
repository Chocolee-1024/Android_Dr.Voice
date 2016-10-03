package com.imac.voice_app.view.setting;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.component.ToolbarView;
import com.imac.voice_app.core.Ruler;
import com.imac.voice_app.module.AlarmPreferences;
import com.imac.voice_app.module.FontManager;

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
    @BindView(R.id.tv_setting_please_set)
    TextView mSettingTitleTextView;
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

    @BindString(R.string.speak_speed_time_default)
    String mTimeDefaultText;
    @BindArray(R.array.week_array)
    String[] mWeekDayTextArray;

    public interface settingRepeatCallBack {
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
                mSettingTitleTextView, mDailyNoticeTitleTextView, mDailyRepeatTextView,
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

    @OnClick({R.id.iv_connection_us, R.id.tv_weekly_notice_week, R.id.tv_daily_notice_time, R.id.tv_weekly_notice_time})
    public void connectionClickListener(View v) {
        switch (v.getId()) {
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