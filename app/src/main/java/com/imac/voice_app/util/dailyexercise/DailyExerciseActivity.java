package com.imac.voice_app.util.dailyexercise;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.imac.voice_app.R;
import com.imac.voice_app.util.login.LoginActivity;
import com.imac.voice_app.view.dailyexercise.DailyExercise;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by isa on 2016/9/19.
 */
public class DailyExerciseActivity extends Activity {

    ArrayList<Integer> topic = new ArrayList<>();
    ArrayList<String> topicList = new ArrayList<>();
    boolean[] isFinish;
    private DailyExercise dailyExerciseView;
    RelativeLayout counterContainer;
    int witch = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_exercise);
        ButterKnife.bind(this);
        getBundle();
        init();
    }

    private void init() {
        dailyExerciseView = new DailyExercise(this);
        dailyExerciseView.changeSelectFragment();
        counterContainer = (RelativeLayout) findViewById(R.id.counter_container);
        for (int i = 0; i < topicList.size(); i++) {
            topic.add(Integer.valueOf(topicList.get(i)));
        }
        isFinish = new boolean[topic.size()];
    }

    private void getBundle() {
        topicList = (ArrayList<String>) getIntent().getExtras().getSerializable(LoginActivity.KEY_DAILY_EXERCISE);
    }

    public ArrayList<Integer> getTopic() {
        return topic;
    }

    public void setCountTextVisible(boolean visible) {
        counterContainer.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public boolean[] isFinish() {
        return isFinish;
    }

    public void setWitch(int witch) {
        this.witch = witch;
    }

    public int getWitch() {
        return witch;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.daily_exercise_container);
        if (fragment instanceof DailyExerciseSelectFragment) {
            super.onBackPressed();
        } else {
            dailyExerciseView.hideCounter();
            dailyExerciseView.changeSelectFragment();
        }
    }
}
