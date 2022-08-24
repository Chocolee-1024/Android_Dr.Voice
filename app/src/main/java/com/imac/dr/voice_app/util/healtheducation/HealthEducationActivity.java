package com.imac.dr.voice_app.util.healtheducation;

import android.app.Activity;
import android.os.Bundle;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.view.healtheducation.HealthEducationView;

public class HealthEducationActivity extends Activity {
    private HealthEducationView healthEducationView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_education);
        healthEducationView = new HealthEducationView(this);
        init();
    }

    private void init(){
        //呼叫changeSelectFragment
        healthEducationView.changeSelectFragment();
    }
}
