package com.imac.voice_app.view.weeklyassessment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.imac.voice_app.R;
import com.imac.voice_app.component.ToolbarView;
import com.imac.voice_app.core.FragmentLauncher;
import com.imac.voice_app.util.weeklyassessment.WeeklyAssessmentActivity;
import com.imac.voice_app.util.weeklyassessment.WeeklyAssessmentStartFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isa on 2016/10/3.
 */
public class WeeklyAssessmentView {
    private Activity activity;
    @BindView(R.id.toolbar)
    ToolbarView toolbar;
    @BindView(R.id.weekly_assessment_container)
    FrameLayout weeklyAssessmentContainer;

    public WeeklyAssessmentView(Activity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);
        init();
    }

    private void init() {
        toolbar.setToolbarButtonCallBack(onToolBarClick());
        change();
    }

    private void change() {
        Bundle bundle = new Bundle();
        bundle.putString(WeeklyAssessmentActivity.KEY_STATUS, WeeklyAssessmentActivity.SOUND_RECORDING);
        FragmentLauncher.change(activity,
                R.id.weekly_assessment_container,
                bundle,
                new WeeklyAssessmentStartFragment().getClass().getName());
    }

    private ToolbarView.toolbarCallBack onToolBarClick() {
        return new ToolbarView.toolbarCallBack() {
            @Override
            public void backButtonListener() {

            }
            @Override
            public void menuButtonListener() {
                activity.finish();
                activity.overridePendingTransition(R.anim.anim_zoom_in_top, R.anim.anim_zoom_out_top);
            }
        };
    }
}
