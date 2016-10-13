package com.imac.voice_app.util.weeklyassessment;

import android.app.Activity;
import android.os.Bundle;

import com.imac.voice_app.R;
import com.imac.voice_app.view.weeklyassessment.WeeklyAssessmentView;

/**
 * Created by isa on 2016/10/3.
 */
public class WeeklyAssessmentActivity extends Activity {

    private WeeklyAssessmentView weeklyAssessmentView;
    public static final String KEY_STATUS = "key_status";
    public static final String KEY_COMPLETE = "key_complete";
    public static final String KEY_SOUND_TOPIC = "key_sound_topic";
    public static final String KEY_WEEKLY_ENABLE_DATE = "key_weekly_enable_date";
    public static final String SOUND_RECORDING = "sound_recording";
    public static final String SELF_ASSESSMENT = "self_assessment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_assessment);
        init();
    }

    private void init() {
        weeklyAssessmentView = new WeeklyAssessmentView(this);
    }
}
