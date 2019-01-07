package com.imac.dr.voice_app.view.weeklyassessment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.component.ToolbarView;
import com.imac.dr.voice_app.core.FragmentLauncher;
import com.imac.dr.voice_app.module.Preferences;
import com.imac.dr.voice_app.module.DataAppend;
import com.imac.dr.voice_app.module.DateChecker;
import com.imac.dr.voice_app.module.database.SqliteManager;
import com.imac.dr.voice_app.util.dailyexercise.DailyExerciseCompleteFragment;
import com.imac.dr.voice_app.util.weeklyassessment.WeeklyAssessmentActivity;
import com.imac.dr.voice_app.util.weeklyassessment.WeeklyAssessmentStartFragment;

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
    private Preferences preferences;
    private String soundTopic = "0";
    private DataWriteEvent dataWriteEvent = null;
    private ArrayList<String> weeklyTopic = null;

    public WeeklyAssessmentContainerView(Activity activity, View view, String status) {
        this.activity = activity;
        this.status = status;
        preferences = new Preferences(activity);
        ButterKnife.bind(this, view);
        assessmentViewPagerAdapter = new WeeklyAssessmentViewPagerAdapter(activity, status);
        init();
    }

    private void init() {
        weeklyTopic = ((WeeklyAssessmentActivity) activity).getWeeklyTopic();
        for (int i = 0; i < weeklyTopic.size(); i++) {
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
                preferences.saveComplete(isComplete);
                change(WeeklyAssessmentActivity.SELF_ASSESSMENT);
            } else {
                isComplete = true;
                DataAppend dataAppend = new DataAppend();
                preferences.saveComplete(isComplete);
                preferences.saveWeeklyEnableDate(new DateChecker(activity).getNextWeekFirstDayTime());
                saveDataToDataBase();
                dataWriteEvent.onDataWrite(
                        dataAppend.formatString(soundTopic),
                        weeklyTopic,
                        assessmentPointArray
                );
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
        SqliteManager sqliteManager = SqliteManager.getInstence(activity);
        sqliteManager.writeWeeklyData(new String[]{ dataAppend.append(weeklyTopic),soundTopic, dataAppend.append(assessmentPointArray)});
    }

    public void setSoundTopic(String soundTopic) {
        if (null != soundTopic) this.soundTopic = soundTopic;
    }

    public void setDataWriteEvent(DataWriteEvent event) {
        dataWriteEvent = event;
    }
}
