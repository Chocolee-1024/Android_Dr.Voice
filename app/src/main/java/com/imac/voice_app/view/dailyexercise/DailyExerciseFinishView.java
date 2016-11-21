package com.imac.voice_app.view.dailyexercise;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.core.FragmentLauncher;
import com.imac.voice_app.module.CountSecond;
import com.imac.voice_app.util.dailyexercise.DailyExerciseActivity;
import com.imac.voice_app.util.dailyexercise.DailyExerciseCompleteFragment;
import com.imac.voice_app.util.dailyexercise.DailyExerciseSelectFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/9/26.
 */
public class DailyExerciseFinishView {
    private Activity activity;
    private CountSecond countSecond;
    @BindView(R.id.daily_exercise_inner_finish_title)
    TextView dailyExerciseFinishTitle;
    @BindView(R.id.daily_exercise_inner_finish_time)
    TextView dailyExerciseFinishTime;
    @BindView(R.id.daily_exercise_inner_finish_close_button)
    ImageView dailyExerciseFinishCloseButton;
    private boolean[] finish;

    public DailyExerciseFinishView(Activity activity, View view, int topicIndex) {
        this.activity = activity;
        ButterKnife.bind(this, view);
        countSecond = new CountSecond(countEvent());
        countSecond.startCountWithCountDown(30);

        finish = ((DailyExerciseActivity) activity).isFinish();
        ArrayList<Integer> topicList = ((DailyExerciseActivity) activity).getTopic();
        for (int i = 0; i < topicList.size(); i++) {
            if (topicList.get(i) == topicIndex) {
                finish[i] = true;
            }
        }
        setFont();
    }

    private void setFont() {
//        FontManager.setFont(activity, FontManager.MEDIUM, dailyExerciseFinishTitle, dailyExerciseFinishTime);
    }

    private String SecToMin(int inputSec) {
        String min = String.valueOf(inputSec / 60);
        String sec = String.valueOf(inputSec % 60);
        if (inputSec % 60 < 10) {
            sec = "0" + sec;
        }
        return "0" + min + ":" + sec;

    }

    private CountSecond.countSecondCallBack countEvent() {
        return new CountSecond.countSecondCallBack() {
            @Override
            public void countPerSecond(int sec) {
                dailyExerciseFinishTime.setText(SecToMin(sec));
                dailyExerciseFinishTime.invalidate();
            }

            @Override
            public void finishCount() {
                if (isComplete()) {
                    changeComplete();
                } else {
                    change();
                }
            }
        };
    }

    @OnClick(R.id.daily_exercise_inner_finish_close_button)
    public void onCloseButtonClick() {
        countSecond.stopCount();
        if (isComplete()) {
            changeComplete();
        } else {
            change();
        }
    }

    private void change() {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.activity_daily_exercise, null);
        FragmentLauncher.change(
                activity,
                view.findViewById(R.id.daily_exercise_container).getId(),
                null,
                new DailyExerciseSelectFragment().getClass().getName()
        );
    }

    private void changeComplete() {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.activity_daily_exercise, null);
        FragmentLauncher.change(
                activity,
                view.findViewById(R.id.daily_exercise_container).getId(),
                null,
                new DailyExerciseCompleteFragment().getClass().getName()
        );
    }

    public CountSecond getCounter() {
        return countSecond;
    }

    private boolean isComplete() {
        boolean complete = true;
        for (int i = 0; i < finish.length; i++) {
            complete = complete & finish[i];
        }
        return complete;
    }
}
