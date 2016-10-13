package com.imac.voice_app.view.weeklyassessment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.imac.voice_app.R;
import com.imac.voice_app.component.ToolbarView;
import com.imac.voice_app.core.FragmentLauncher;
import com.imac.voice_app.core.PreferencesHelper;
import com.imac.voice_app.module.DataAppend;
import com.imac.voice_app.module.DateChecker;
import com.imac.voice_app.module.SharePreferencesManager;
import com.imac.voice_app.module.database.SqliteManger;
import com.imac.voice_app.util.dailyexercise.DailyExerciseCompleteFragment;
import com.imac.voice_app.util.login.LoginActivity;
import com.imac.voice_app.util.weeklyassessment.WeeklyAssessmentActivity;
import com.imac.voice_app.util.weeklyassessment.WeeklyAssessmentStartFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by isa on 2016/10/4.
 */
public class WeeklyAssessmentContainerView implements ViewPager.OnPageChangeListener {
    @BindView(R.id.view_pager_container)
    ViewPager viewPagerContainer;
    @BindView(R.id.next_step_button)
    Button nextStepButton;
    @BindView(R.id.previous_step_button)
    Button previousStepButton;
    private Activity activity;
    private WeeklyAssessmentViewPagerAdapter assessmentViewPagerAdapter;
    private String status;
    private boolean isComplete = false;
    private ArrayList<String> soundPointArray = new ArrayList<>();
    private ArrayList<String> assessmentPointArray = new ArrayList<>();
    private SharePreferencesManager sharePreferencesManager;
    private String soundTopic;

    public WeeklyAssessmentContainerView(Activity activity, View view, String status) {
        this.activity = activity;
        this.status = status;
        sharePreferencesManager = SharePreferencesManager.getInstance(activity);
        ButterKnife.bind(this, view);
        assessmentViewPagerAdapter = new WeeklyAssessmentViewPagerAdapter(activity, status);
        init();
    }

    private void init() {
        for (int i = 0; i < 7; i++) {
            soundPointArray.add("0");
        }
        for (int i = 0; i < 10; i++) {
            assessmentPointArray.add("0");
        }
        if (status.equals(WeeklyAssessmentActivity.SOUND_RECORDING)) {
            ((ToolbarView) activity.findViewById(R.id.toolbar)).setTitleTextViewText(activity.getResources().getString(R.string.weekly_assessment_sound_title));
            assessmentViewPagerAdapter.setRadioButtonArray(soundPointArray);
        } else {
            ((ToolbarView) activity.findViewById(R.id.toolbar)).setTitleTextViewText(activity.getResources().getString(R.string.weekly_assessment_self_title));
            assessmentViewPagerAdapter.setRadioButtonArray(assessmentPointArray);
        }
        viewPagerContainer.setAdapter(assessmentViewPagerAdapter);
        viewPagerContainer.addOnPageChangeListener(this);
        previousStepButton.setVisibility(View.INVISIBLE);
        nextStepButton.setVisibility(View.VISIBLE);

    }

    @OnTouch(R.id.view_pager_container)
    public boolean onTouch() {
        return true;
    }

    @OnClick(R.id.previous_step_button)
    public void setPreviousStepButton() {
        if (status.equals(WeeklyAssessmentActivity.SOUND_RECORDING)) {
            assessmentViewPagerAdapter.setRadioButtonArray(soundPointArray);
        } else {
            assessmentViewPagerAdapter.setRadioButtonArray(assessmentPointArray);
        }
        viewPagerContainer.setCurrentItem(viewPagerContainer.getCurrentItem() - 1);
    }

    @OnClick(R.id.next_step_button)
    public void setNextStepButtonClick() {
        if (viewPagerContainer.getCurrentItem() == assessmentViewPagerAdapter.getCount() - 1) {
            if (status.equals(WeeklyAssessmentActivity.SOUND_RECORDING)) {
                isComplete = false;
                sharePreferencesManager.save(PreferencesHelper.Type.BOOLEAN, WeeklyAssessmentActivity.KEY_COMPLETE, isComplete);
                change(WeeklyAssessmentActivity.SELF_ASSESSMENT);
            } else {
                isComplete = true;
                sharePreferencesManager.save(PreferencesHelper.Type.BOOLEAN, WeeklyAssessmentActivity.KEY_COMPLETE, isComplete);
                sharePreferencesManager.save(PreferencesHelper.Type.LONG, WeeklyAssessmentActivity.KEY_WEEKLY_ENABLE_DATE, new DateChecker(activity).getNextWeekFirstDayTime());
                saveDataToDataBase();
                change(null);
            }
        } else {
            viewPagerContainer.setCurrentItem(viewPagerContainer.getCurrentItem() + 1);
        }
    }

    private void change(String nextStatus) {
        if (nextStatus != null) {
            soundPointArray = assessmentViewPagerAdapter.getRadioButtonArray();
            Bundle bundle = new Bundle();
            DataAppend dataAppend = new DataAppend();
            bundle.putString(WeeklyAssessmentActivity.KEY_STATUS, nextStatus);
            bundle.putString(WeeklyAssessmentActivity.KEY_SOUND_TOPIC, dataAppend.append(soundPointArray));
            FragmentLauncher.change(activity,
                    R.id.weekly_assessment_container,
                    bundle,
                    new WeeklyAssessmentStartFragment().getClass().getName());
        } else {
            FragmentLauncher.change(activity,
                    R.id.weekly_assessment_container,
                    null,
                    new DailyExerciseCompleteFragment().getClass().getName());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        nextStepButton.setText(R.string.daily_exercise_next_step);
        if (position == assessmentViewPagerAdapter.getCount() - 1) {
            nextStepButton.setText(R.string.weekly_assessment_successful);
        } else {
            nextStepButton.setText(R.string.daily_exercise_next_step);
        }
        if (position != 0) {
            previousStepButton.setVisibility(View.VISIBLE);
        } else {
            previousStepButton.setVisibility(View.INVISIBLE);
        }
        assessmentViewPagerAdapter.setPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void saveDataToDataBase() {
        DataAppend dataAppend = new DataAppend();
        SqliteManger sqliteManger = new SqliteManger(activity);
        String account = (String) sharePreferencesManager.get(LoginActivity.KEY_LOGIN_ACCOUNT, PreferencesHelper.Type.STRING);
        sqliteManger.write(new String[]{account, soundTopic, dataAppend.append(assessmentPointArray)});
    }

    public void setSoundTopic(String soundTopic) {
        this.soundTopic = soundTopic;
    }

}
