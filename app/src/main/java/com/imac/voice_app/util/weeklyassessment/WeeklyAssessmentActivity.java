package com.imac.voice_app.util.weeklyassessment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.imac.voice_app.R;
import com.imac.voice_app.module.permission.PermissionsActivity;
import com.imac.voice_app.module.permission.PermissionsChecker;
import com.imac.voice_app.util.login.LoginActivity;
import com.imac.voice_app.view.weeklyassessment.WeeklyAssessmentView;

import java.util.ArrayList;

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
    private ArrayList<String> weeklyTopicList = null;
    private PermissionsChecker permissionsChecker = null;
    private final String[] permission = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final static int ASK_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_assessment);
        checkPermission();
    }

    private void checkPermission() {
        permissionsChecker = new PermissionsChecker(this);
        if (permissionsChecker.missingPermissions(permission)) {
            PermissionsActivity.startPermissionsForResult(this, ASK_PERMISSION_CODE, permission);
        } else {
            getBundle();
            init();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PermissionsActivity.PERMISSIONS_ACCEPT && requestCode == ASK_PERMISSION_CODE) {
            getBundle();
            init();
        }
    }

    private void getBundle() {
        weeklyTopicList = (ArrayList<String>) getIntent().getExtras().getSerializable(LoginActivity.KEY_WEEKLY_EXERCISE);
    }

    private void init() {
        weeklyAssessmentView = new WeeklyAssessmentView(this);
    }

    public ArrayList<String> getWeeklyTopic() {
        return weeklyTopicList;
    }
}
