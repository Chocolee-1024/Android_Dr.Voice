package com.imac.voice_app.view.dailyexercise;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.imac.voice_app.R;
import com.imac.voice_app.component.CustomIndicator;
import com.imac.voice_app.core.FragmentLauncher;
import com.imac.voice_app.util.dailyexercise.DailyExerciseActivity;
import com.imac.voice_app.util.dailyexercise.DailyExerciseInnerSelectFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/9/20.
 */
public class DailySelectExerciseView implements ViewPager.OnPageChangeListener {
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
        hideCountDown();
        adapter = new DailySelectExerciseAdapter(activity, topic);
        fragmentContainer.setAdapter(adapter);
        fragmentContainer.addOnPageChangeListener(this);
        indicator.setIsFinish(isFinish);
        indicator.setViewPager(fragmentContainer);
        fragmentContainer.setCurrentItem(((DailyExerciseActivity) activity).getWitch());
        previousStepButton.setVisibility(View.VISIBLE);
        nextStepButton.setVisibility(View.VISIBLE);
        if (((DailyExerciseActivity) activity).getWitch() == 0)
            previousStepButton.setVisibility(View.INVISIBLE);
        else if (((DailyExerciseActivity) activity).getWitch() == topic.size() - 1)
            nextStepButton.setVisibility(View.INVISIBLE);
    }

    private void setFont() {
    }
// TODO: 2016/10/24  viewPager 改為可滑動
//    @OnTouch(R.id.fragment_container)
//    public boolean onTouch() {
//        return true;
//    }

    @OnClick(R.id.next_step_button)
    public void onNextButtonClick() {
        fragmentContainer.setCurrentItem(fragmentContainer.getCurrentItem() + 1, true);
    }

    @OnClick(R.id.previous_step_button)
    public void onPreviousButtonClick() {
        fragmentContainer.setCurrentItem(fragmentContainer.getCurrentItem() - 1, true);
    }

    @OnClick(R.id.start_button)
    public void onStartButtonClick() {
        ((DailyExerciseActivity) activity).setWitch(fragmentContainer.getCurrentItem());
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == adapter.getCount() - 1) {
            nextStepButton.setVisibility(View.INVISIBLE);
            previousStepButton.setVisibility(View.VISIBLE);
        } else if (position == 0) {
            nextStepButton.setVisibility(View.VISIBLE);
            previousStepButton.setVisibility(View.INVISIBLE);
        } else {
            nextStepButton.setVisibility(View.VISIBLE);
            previousStepButton.setVisibility(View.VISIBLE);
        }
    }

    public void hideCountDown() {
        RelativeLayout counterContainer = (RelativeLayout) activity.findViewById(R.id.counter_container);
        counterContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
