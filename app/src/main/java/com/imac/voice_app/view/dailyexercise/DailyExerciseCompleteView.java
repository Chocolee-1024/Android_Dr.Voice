package com.imac.voice_app.view.dailyexercise;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.component.ToolbarView;
import com.imac.voice_app.module.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/9/27.
 */
public class DailyExerciseCompleteView {
    private Activity activity;
    @BindView(R.id.daily_exercise_finish_title)
    TextView dailyExerciseFinishTitle;
    @BindView(R.id.daily_exercise_finish_image)
    ImageView dailyExerciseFinishImage;
    @BindView(R.id.daily_exercise_finish_button)
    Button dailyExerciseFinishButton;
    private ToolbarView toolbarView;

    public DailyExerciseCompleteView(Activity activity, View view) {
        this.activity = activity;
        ButterKnife.bind(this, view);
        setFont();
    }
    private void setFont() {
        FontManager.setFont(activity, FontManager.MEDIUM, dailyExerciseFinishTitle, dailyExerciseFinishButton);
    }

    @OnClick(R.id.daily_exercise_finish_button)
    public void onBackMenuClick() {
        activity.finish();
    }
}
