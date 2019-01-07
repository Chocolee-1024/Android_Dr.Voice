package com.imac.dr.voice_app.view.weeklyassessment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.component.ToolbarView;
import com.imac.dr.voice_app.core.FragmentLauncher;
import com.imac.dr.voice_app.util.weeklyassessment.WeeklyAssessmentActivity;
import com.imac.dr.voice_app.util.weeklyassessment.WeeklyAssessmentContainerFragment;

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
    private String soundTopic;

    public WeeklyAssessmentStartView(Activity activity, View view, String status) {
        this.activity = activity;
        this.status = status;
        ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        ((ToolbarView) activity.findViewById(R.id.toolbar)).setTitleTextViewText(activity.getResources().getString(R.string.weekly_assessment_toolbar_title));
        if (0==((WeeklyAssessmentActivity) activity).getWeeklyTopic().size())
            status = WeeklyAssessmentActivity.SELF_ASSESSMENT;
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
        Bundle bundle = new Bundle();
        bundle.putString(WeeklyAssessmentActivity.KEY_STATUS, status);
        if (status.equals(WeeklyAssessmentActivity.SELF_ASSESSMENT)) {
            bundle.putString(WeeklyAssessmentActivity.KEY_SOUND_TOPIC, soundTopic);
        }
        FragmentLauncher.change(activity,
                R.id.weekly_assessment_container,
                bundle,
                new WeeklyAssessmentContainerFragment().getClass().getName());
    }

    public void setSoundTopic(String soundTopic) {
        this.soundTopic = soundTopic;
    }
}
