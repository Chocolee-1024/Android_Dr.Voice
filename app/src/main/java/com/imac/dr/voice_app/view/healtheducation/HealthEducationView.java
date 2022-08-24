package com.imac.dr.voice_app.view.healtheducation;

import android.app.Activity;
import android.util.Log;

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

    private  ToolbarView.toolbarCallBack toolbarCallBack = new ToolbarView.toolbarCallBack() {
        @Override
        public void backButtonListener() {
        }

        @Override
        public void menuButtonListener() {
            //就如下排的返回建
            activity.onBackPressed();
        }
    };
    //HealthEducationView的建構值
    public HealthEducationView(Activity activity){
        this.activity=activity;
        ButterKnife.bind(this,activity);
        //呼叫setToolbarButtonCallBack
        toolbar.setToolbarButtonCallBack(toolbarCallBack);
    }
    //呼叫換Fragment的方法
    public void changeSelectFragment() {
        FragmentLauncher.change(
                activity,
                R.id.health_education_container,
                null,
                new HealthEducationContainerFragment().getClass().getName()
        );
    }
}
