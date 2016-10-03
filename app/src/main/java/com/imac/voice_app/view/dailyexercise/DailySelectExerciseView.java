package com.imac.voice_app.view.dailyexercise;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.imac.voice_app.R;
import com.imac.voice_app.component.CustomIndicator;
import com.imac.voice_app.core.FragmentLauncher;
import com.imac.voice_app.module.FontManager;
import com.imac.voice_app.util.dailyexercise.DailyExerciseActivity;
import com.imac.voice_app.util.dailyexercise.DailyExerciseInnerSelectFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by isa on 2016/9/20.
 */
public class DailySelectExerciseView {
    public static final String KEY_INNER_FRAGMENT_INDEX = "key_inner_fragment_index";
    private Activity activity;
    @BindView(R.id.fragment_container)
    ViewPager fragmentContainer;
    @BindView(R.id.next_step_button)
    Button nextStepButton;
    @BindView(R.id.previous_step_button)
    Button previousStepButton;
    @BindView(R.id.indicator)
    CustomIndicator indicator;
    @BindView(R.id.start_button)
    Button startButton;
    private DailySelectExerciseAdapter adapter;

    ArrayList<Integer> topic;
    boolean[] isFinish;

    public DailySelectExerciseView(Activity activity, View view) {
        this.activity = activity;
        ButterKnife.bind(this, view);
        topic = ((DailyExerciseActivity) activity).getTopic();
        isFinish = ((DailyExerciseActivity) activity).isFinish();
        init();
    }

    private void init() {
        setFont();
        adapter = new DailySelectExerciseAdapter(activity, topic);
        fragmentContainer.setAdapter(adapter);
        indicator.setIsFinish(isFinish);
        indicator.setViewPager(fragmentContainer);
        previousStepButton.setVisibility(View.INVISIBLE);
    }

    private void setFont() {
        FontManager.setFont(activity, FontManager.MEDIUM, previousStepButton, nextStepButton, startButton);
    }

    @OnTouch(R.id.fragment_container)
    public boolean onTouch() {
        return true;
    }

    @OnClick(R.id.next_step_button)
    public void onNextButtonClick() {
        if (fragmentContainer.getCurrentItem() == adapter.getCount() - 2) {
            fragmentContainer.setCurrentItem(fragmentContainer.getCurrentItem() + 1, true);
            nextStepButton.setVisibility(View.INVISIBLE);
            previousStepButton.setVisibility(View.VISIBLE);
        } else if (fragmentContainer.getCurrentItem() < adapter.getCount() - 1) {
            previousStepButton.setVisibility(View.VISIBLE);
            fragmentContainer.setCurrentItem(fragmentContainer.getCurrentItem() + 1, true);
        }
    }

    @OnClick(R.id.previous_step_button)
    public void onPreviousButtonClick() {
        if (fragmentContainer.getCurrentItem() == 1) {
            fragmentContainer.setCurrentItem(fragmentContainer.getCurrentItem() - 1, true);
            previousStepButton.setVisibility(View.INVISIBLE);
            nextStepButton.setVisibility(View.VISIBLE);
        } else if (fragmentContainer.getCurrentItem() > 0) {
            nextStepButton.setVisibility(View.VISIBLE);
            fragmentContainer.setCurrentItem(fragmentContainer.getCurrentItem() - 1, true);
        }
    }

    @OnClick(R.id.start_button)
    public void onStartButtonClick() {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_INNER_FRAGMENT_INDEX, topic.get(fragmentContainer.getCurrentItem()));
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.activity_daily_exercise, null);
        FragmentLauncher.change(
                activity,
                view.findViewById(R.id.daily_exercise_container).getId(),
                bundle,
                new DailyExerciseInnerSelectFragment().getClass().getName()
        );
    }

    private boolean isComplete() {
        boolean complete = true;
        for (int i = 0; i < isFinish.length; i++) {
            complete = complete & isFinish[i];
        }
        return complete;
    }

    public void addToBackStack(String stackName) {
        activity.getFragmentManager().beginTransaction().addToBackStack(stackName).commit();
    }

}
