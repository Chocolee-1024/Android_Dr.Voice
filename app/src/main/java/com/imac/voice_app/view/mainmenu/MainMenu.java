package com.imac.voice_app.view.mainmenu;

import android.app.Activity;
import android.support.percent.PercentRelativeLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.module.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/9/8.
 */
public class MainMenu {
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

    private  MenuClickListener menuClickListener;
    public MainMenu(Activity activity,MenuClickListener menuClickListener) {
        this.activity = activity;
        ButterKnife.bind(this, activity);
        this.menuClickListener=menuClickListener;
        setFontType();
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

}
