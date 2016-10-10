package com.imac.voice_app.view.weeklyassessment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.core.FragmentLauncher;
import com.imac.voice_app.util.weeklyassessment.WeeklyAssessmentActivity;
import com.imac.voice_app.util.weeklyassessment.WeeklyAssessmentContainerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isa on 2016/10/3.
 */
public class WeeklyAssessmentStartView {
    @BindView(R.id.weekly_assessment_sound_title)
    TextView weeklyAssessmentSoundTitle;
    @BindView(R.id.weekly_assessment_sound_img)
    ImageView weeklyAssessmentSoundImg;
    @BindView(R.id.weekly_assessment_sound_button)
    Button weeklyAssessmentSoundButton;
    private Activity activity;
    private String status;

    public WeeklyAssessmentStartView(Activity activity, View view, String status) {
        this.activity = activity;
        this.status = status;
        ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        if (status.equals(WeeklyAssessmentActivity.SOUND_RECORDING)) {
            weeklyAssessmentSoundTitle.setText(R.string.weekly_assessment_sound_title);
            weeklyAssessmentSoundImg.setImageResource(R.drawable.history_1_icon);
        } else if (status.equals(WeeklyAssessmentActivity.SELF_ASSESSMENT)) {
            weeklyAssessmentSoundTitle.setText(R.string.weekly_assessment_self_assessment_title);
            weeklyAssessmentSoundImg.setImageResource(R.drawable.history_2_icon);
        }
    }

    @OnClick(R.id.weekly_assessment_sound_button)
    public void onStartClick() {
        change();
    }

    private void change() {
        Bundle bundle =new Bundle();
        bundle.putString(WeeklyAssessmentActivity.KEY_STATUS,status);
        FragmentLauncher.change(activity,
                R.id.weekly_assessment_container,
                bundle,
                new WeeklyAssessmentContainerFragment().getClass().getName());
    }
}
