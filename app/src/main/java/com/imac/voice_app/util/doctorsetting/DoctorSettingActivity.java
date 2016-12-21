package com.imac.voice_app.util.doctorsetting;

import android.app.Activity;
import android.os.Bundle;

import com.imac.voice_app.R;
import com.imac.voice_app.core.FragmentLauncher;
import com.imac.voice_app.view.doctorsetting.DailySettingView;

/**
 * Created by isa on 2016/12/6.
 */

public class DoctorSettingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_setting);
        init();
    }

    private void init() {
        FragmentLauncher.change(this, R.id.container, null, InputPasswordFragment.class.getName());
    }
}
