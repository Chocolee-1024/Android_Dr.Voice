package com.imac.dr.voice_app.view.healtheducation;

import android.app.Activity;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.component.ToolbarView;
import com.imac.dr.voice_app.core.FragmentLauncher;
import com.imac.dr.voice_app.util.healtheducation.HealthEducationContainerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HealthEducationView {
    private  Activity activity;
    @BindView(R.id.toolbar)
    ToolbarView toolbar;
    public HealthEducationView(Activity activity){
        this.activity=activity;
        ButterKnife.bind(this,activity);
        toolbar.setToolbarButtonCallBack(toolbarCallBack);

    }
    private  ToolbarView.toolbarCallBack toolbarCallBack = new ToolbarView.toolbarCallBack() {
        @Override
        public void backButtonListener() {

        }

        @Override
        public void menuButtonListener() {
            activity.onBackPressed();
        }
    };
    public void changeSelectFragment() {
        FragmentLauncher.change(
                activity,
                R.id.health_education_container,
                null,
                new HealthEducationContainerFragment().getClass().getName()
        );
    }
}
