package com.imac.voice_app.util.doctorsetting;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.imac.voice_app.R;
import com.imac.voice_app.component.ToolbarView;
import com.imac.voice_app.core.FragmentLauncher;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isa on 2016/12/6.
 */

public class DoctorSettingActivity extends Activity {
    @BindView(R.id.toolbar)
    ToolbarView toolbar;
    @BindView(R.id.container)
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_setting);
        ButterKnife.bind(this);
        toolbar.setToolbarButtonCallBack(new ToolbarView.toolbarCallBack() {
            @Override
            public void backButtonListener() {

            }

            @Override
            public void menuButtonListener() {
                finish();
            }
        });
        init();
    }

    private void init() {
        FragmentLauncher.change(this, R.id.container, null, InputPasswordFragment.class.getName());
    }

    public ToolbarView getToolBarView(){
        return toolbar;
    }
}
