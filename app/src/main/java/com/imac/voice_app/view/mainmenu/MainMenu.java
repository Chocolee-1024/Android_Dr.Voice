package com.imac.voice_app.view.mainmenu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.core.PreferencesHelper;
import com.imac.voice_app.module.DataAppend;
import com.imac.voice_app.module.DateChecker;
import com.imac.voice_app.module.FontManager;
import com.imac.voice_app.util.login.LoginActivity;
import com.imac.voice_app.util.weeklyassessment.WeeklyAssessmentActivity;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/9/8.
 */
public class MainMenu extends PreferencesHelper {
    private Activity activity;
    @BindView(R.id.daily_exercise_icon)
    ImageView dailyExerciseIcon;
    @BindView(R.id.daily_exercise_text)
    TextView dailyExerciseText;
    @BindView(R.id.daily_exercise_container)
    PercentRelativeLayout dailyExerciseContainer;
    @BindView(R.id.weekly_assessment_icon)
    ImageView weeklyAssessmentIcon;
    @BindView(R.id.weekly_assessment_text)
    TextView weeklyAssessmentText;
    @BindView(R.id.weekly_assessment_container)
    PercentRelativeLayout weeklyAssessmentContainer;
    @BindView(R.id.history_icon)
    ImageView historyIcon;
    @BindView(R.id.history_text)
    TextView historyText;
    @BindView(R.id.history_container)
    PercentRelativeLayout historyContainer;
    @BindView(R.id.speak_speed_icon)
    ImageView speakSpeedIcon;
    @BindView(R.id.speak_speed_text)
    TextView speakSpeedText;
    @BindView(R.id.speak_speed_container)
    PercentRelativeLayout speakSpeedContainer;
    @BindView(R.id.setting_icon)
    ImageView settingIcon;
    @BindView(R.id.setting_text)
    TextView settingText;
    @BindView(R.id.setting_container)
    PercentRelativeLayout settingContainer;
    private Bundle bundle;
    private MenuClickListener menuClickListener;

    public MainMenu(Activity activity, MenuClickListener menuClickListener, Bundle bundle) {
        super(activity);
        this.activity = activity;
        this.bundle = bundle;
        ButterKnife.bind(this, activity);
        this.menuClickListener = menuClickListener;
        getBundle();
        setFontType();
    }

    public void weeklyAssessmentEnabler() {
        DateChecker dateChecker = new DateChecker(activity);
        boolean isOverDay = dateChecker.isOverDay(Calendar.getInstance());
        if (isOverDay) {
            save(Type.BOOLEAN, WeeklyAssessmentActivity.KEY_COMPLETE, false);
            weeklyAssessmentContainer.setClickable(true);
            weeklyAssessmentText.setTextColor(Color.BLACK);
        } else {
            if ((Boolean) get(WeeklyAssessmentActivity.KEY_COMPLETE, Type.BOOLEAN)) {
                weeklyAssessmentContainer.setClickable(false);
                weeklyAssessmentText.setTextColor(Color.GRAY);
            } else {
                weeklyAssessmentContainer.setClickable(true);
                weeklyAssessmentText.setTextColor(Color.BLACK);
            }
        }
    }

    private void getBundle() {
        if ("".equals(get(LoginActivity.KEY_LOGIN_ACCOUNT, Type.STRING))) {
            String loginAccount = bundle.getString(LoginActivity.KEY_LOGIN_ACCOUNT);
            String loginName = bundle.getString(LoginActivity.KEY_LOGIN_NAME);
            ArrayList<String> topicList = (ArrayList<String>) bundle.getSerializable(LoginActivity.KEY_DAILY_EXERCISE);

            DataAppend dataAppend = new DataAppend();
            save(Type.STRING, LoginActivity.KEY_LOGIN_ACCOUNT, loginAccount);
            save(Type.STRING, LoginActivity.KEY_LOGIN_NAME, loginName);
            save(Type.STRING, LoginActivity.KEY_DAILY_EXERCISE, dataAppend.append(topicList));
        }
    }

    private void setFontType() {
        FontManager.setFont(activity, FontManager.NORMAL, dailyExerciseText, weeklyAssessmentText, historyText, speakSpeedText, settingText);
    }

    @OnClick(R.id.daily_exercise_container)
    public void onDailyExerciseClick() {
        menuClickListener.onDailyExerciseClick();
    }

    @OnClick(R.id.weekly_assessment_container)
    public void onWeeklyAssessmentClick() {
        menuClickListener.onWeeklyAssessmentClick();
    }

    @OnClick(R.id.history_container)
    public void onHistoryClick() {
        menuClickListener.onHistoryClick();
    }

    @OnClick(R.id.speak_speed_container)
    public void onSpeakSpeedClick() {
        menuClickListener.onSpeakSpeedClick();
    }

    @OnClick(R.id.setting_container)
    public void onSettingClick() {
        menuClickListener.onSettingClick();
    }

    @Override
    public String getClassName() {
        return activity.getPackageName();
    }
}
