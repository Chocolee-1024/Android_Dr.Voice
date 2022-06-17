package com.imac.dr.voice_app.util.dailyexercise;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.util.login.LoginActivity;
import com.imac.dr.voice_app.view.dailyexercise.DailyExercise;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by isa on 2016/9/19.
 */
public class DailyExerciseActivity extends Activity {

    private ArrayList<Integer> topic = new ArrayList<>();
    private ArrayList<String> topicList = new ArrayList<>();
    private boolean[] isFinish;
    private DailyExercise dailyExerciseView;
    private RelativeLayout counterContainer;
    private int witch = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_exercise);
        ButterKnife.bind(this);
        getBundle();
        init();
    }

    private void init() {
        Log.d("noteeee", "topicList: "+topicList);
        dailyExerciseView = new DailyExercise(this);
        dailyExerciseView.changeSelectFragment();
        counterContainer = (RelativeLayout) findViewById(R.id.counter_container);
        for (int i = 0; i < topicList.size(); i++) {
            int topicValue = Integer.valueOf(topicList.get(i));
            topic.add(topicValue);
            Log.d("noteeee", "topic: "+topic);
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
