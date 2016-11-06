package com.imac.voice_app.view.history;

import android.app.Activity;
import android.support.percent.PercentRelativeLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imac.voice_app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/10/24.
 */
public class HistoryFragmentView {
    @BindView(R.id.voice_history_icon)
    ImageView voiceHistoryIcon;
    @BindView(R.id.voice_history_text)
    TextView voiceHistoryText;
    @BindView(R.id.voice_history)
    PercentRelativeLayout voiceHistory;
    @BindView(R.id.daily_exercise_history_icon)
    ImageView dailyExerciseHistoryIcon;
    @BindView(R.id.exercise_history_text)
    TextView exerciseHistoryText;
    @BindView(R.id.daily_exercise_history)
    PercentRelativeLayout dailyExerciseHistory;
    private Activity activity = null;
    private onClickEvent event = null;

    public HistoryFragmentView(Activity activity, View view, onClickEvent event) {
        this.activity = activity;
        this.event = event;
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.voice_history)
    public void voiceHistoryClick() {
        event.onVoiceHistoryClick();
    }

    @OnClick(R.id.daily_exercise_history)
    public void voiceDailyExerciseHistory() {
        event.onVoiceDailyExerciseHistory();
    }

    public interface onClickEvent {
        void onVoiceHistoryClick();

        void onVoiceDailyExerciseHistory();
    }
}
