package com.imac.dr.voice_app.view.mainmenu;

import android.app.Activity;
import android.graphics.Color;
import androidx.percentlayout.widget.PercentRelativeLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.module.DateChecker;
import com.imac.dr.voice_app.module.Preferences;

import java.util.Calendar;

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
    @BindView(R.id.health_education_container)
    PercentRelativeLayout healthEducationContainer;
    private MenuClickListener menuClickListener;
    private Preferences preferences;

    //MainMenu的建構值
    public MainMenu(Activity activity, MenuClickListener menuClickListener) {
        this.activity = activity;
        preferences = new Preferences(activity);
        ButterKnife.bind(this, activity);
        this.menuClickListener = menuClickListener;
    }
    public void weeklyAssessmentEnabler() {
        DateChecker dateChecker = new DateChecker(activity);
        //拿取距離上次做練習是否超過一個禮拜
        boolean isOverWeek = dateChecker.isOverWeek(Calendar.getInstance());
        //如果 isOverWeek==True 開啟每周練習
        if (isOverWeek) {
            //每周練習存入還沒做過(false)
            preferences.saveComplete(false);
            weeklyAssessmentContainer.setClickable(true);
            weeklyAssessmentText.setTextColor(Color.BLACK);
        }else{
            weeklyAssessmentContainer.setClickable(false);
            weeklyAssessmentText.setTextColor(Color.GRAY);
        }
//         else {
//            //判斷每周練習是否做過(這一個禮拜內是否做過了)
//            if (preferences.getComplete()) {
//                weeklyAssessmentContainer.setClickable(false);
//                weeklyAssessmentText.setTextColor(Color.GRAY);
//            } else {
//                weeklyAssessmentContainer.setClickable(true);
//                weeklyAssessmentText.setTextColor(Color.BLACK);
//            }
//        }

    }

    public void speedEnabler() {
        //判斷語速設定是否開啟
        if (preferences.getSpeedDoctorSetting()) {
            //讓按鈕不可按
            speakSpeedContainer.setClickable(false);
            speakSpeedText.setTextColor(Color.GRAY);
        } else {
            //讓按鈕可按
            speakSpeedContainer.setClickable(true);
            speakSpeedText.setTextColor(Color.BLACK);
        }
    }
    //每日練習按鈕監聽
    @OnClick(R.id.daily_exercise_container)
    public void onDailyExerciseClick() {
        menuClickListener.onDailyExerciseClick();
    }
    //每周練習按鈕監聽
    @OnClick(R.id.weekly_assessment_container)
    public void onWeeklyAssessmentClick() {
        menuClickListener.onWeeklyAssessmentClick();
    }
    //歷史紀錄按鈕監聽
    @OnClick(R.id.history_container)
    public void onHistoryClick() {
        menuClickListener.onHistoryClick();
    }
    //語速按鈕監聽
    @OnClick(R.id.speak_speed_container)
    public void onSpeakSpeedClick() {
        menuClickListener.onSpeakSpeedClick();
    }
    //設定按鈕監聽
    @OnClick(R.id.setting_container)
    public void onSettingClick() {
        menuClickListener.onSettingClick();
    }
    //噪音衛教按鈕監聽
    @OnClick(R.id.health_education_container)
    public void onEductionClick() {
        menuClickListener.onEductionClick();
    }
}
