package com.imac.voice_app.view.weeklyassessment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.imac.voice_app.R;
import com.imac.voice_app.core.FragmentLauncher;
import com.imac.voice_app.core.PreferencesHelper;
import com.imac.voice_app.module.DataAppend;
import com.imac.voice_app.module.DateChecker;
import com.imac.voice_app.module.SqliteManger;
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
public class WeeklyAssessmentContainerView extends PreferencesHelper implements ViewPager.OnPageChangeListener {
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
    private ArrayList<String> assessmentPoint = new ArrayList<>();

    public WeeklyAssessmentContainerView(Activity activity, View view, String status) {
        super(activity);
        this.activity = activity;
        this.status = status;
        ButterKnife.bind(this, view);
        assessmentViewPagerAdapter = new WeeklyAssessmentViewPagerAdapter(activity, status);
        init();
    }

    private void init() {
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
        viewPagerContainer.setCurrentItem(viewPagerContainer.getCurrentItem() - 1);
    }

    @OnClick(R.id.next_step_button)
    public void setNextStepButtonClick() {
        if (viewPagerContainer.getCurrentItem() == assessmentViewPagerAdapter.getCount() - 1) {
            if (status.equals(WeeklyAssessmentActivity.SOUND_RECORDING)) {
                isComplete = false;
                soundPointArray.add(viewPagerContainer.getCurrentItem(), assessmentViewPagerAdapter.getOptionPoint());
                save(Type.BOOLEAN, WeeklyAssessmentActivity.KEY_COMPLETE, isComplete);
                save(Type.STRING, WeeklyAssessmentActivity.KEY_WHICH_FRAGMENT, WeeklyAssessmentActivity.SELF_ASSESSMENT);
                saveDataToDataBase(soundPointArray, status);
                change();
            } else {
                isComplete = true;
                assessmentPoint.add(viewPagerContainer.getCurrentItem(), assessmentViewPagerAdapter.getOptionPoint());
                save(Type.STRING, WeeklyAssessmentActivity.KEY_WHICH_FRAGMENT, WeeklyAssessmentActivity.SOUND_RECORDING);
                save(Type.BOOLEAN, WeeklyAssessmentActivity.KEY_COMPLETE, isComplete);
                save(Type.LONG, WeeklyAssessmentActivity.KEY_WEEKLY_ENABLE_DATE, new DateChecker(activity).getNextWeekFirstDayTime());
                saveDataToDataBase(assessmentPoint, status);
                FragmentLauncher.change(
                        activity,
                        R.id.weekly_assessment_container,
                        null,
                        new DailyExerciseCompleteFragment().getClass().getName()
                );
            }
        } else {
            if (status.equals(WeeklyAssessmentActivity.SOUND_RECORDING)) {
                soundPointArray.add(viewPagerContainer.getCurrentItem(), assessmentViewPagerAdapter.getOptionPoint());
            } else {
                assessmentPoint.add(viewPagerContainer.getCurrentItem(), assessmentViewPagerAdapter.getOptionPoint());
            }
            viewPagerContainer.setCurrentItem(viewPagerContainer.getCurrentItem() + 1);
        }
        assessmentViewPagerAdapter.setOptionPoint("0");
    }

    private void change() {
        Bundle bundle = new Bundle();
        bundle.putString(WeeklyAssessmentActivity.KEY_STATUS, WeeklyAssessmentActivity.SELF_ASSESSMENT);
        FragmentLauncher.change(activity,
                R.id.weekly_assessment_container,
                bundle,
                new WeeklyAssessmentStartFragment().getClass().getName());
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
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public String getClassName() {
        return activity.getPackageName();
    }

    private void saveDataToDataBase(ArrayList<String> saveData, String status) {
        DataAppend dataAppend = new DataAppend();
        SqliteManger sqliteManger = new SqliteManger(activity);
        String account = (String) get(LoginActivity.KEY_LOGIN_ACCOUNT, Type.STRING);
        if (status.equals(WeeklyAssessmentActivity.SOUND_RECORDING)) {
            sqliteManger.writeSoundPoint(new String[]{account, dataAppend.append(saveData)});
        } else if (status.equals(WeeklyAssessmentActivity.SELF_ASSESSMENT)) {
            sqliteManger.writeAssessmentPoint(new String[]{account, dataAppend.append(saveData)});
        } else {
            throw new RuntimeException("invalid status");
        }

    }
}
