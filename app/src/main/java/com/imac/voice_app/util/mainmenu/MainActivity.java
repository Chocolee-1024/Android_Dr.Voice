package com.imac.voice_app.util.mainmenu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.imac.voice_app.R;
import com.imac.voice_app.core.ActivityLauncher;
import com.imac.voice_app.module.DataAppend;
import com.imac.voice_app.module.Preferences;
import com.imac.voice_app.module.database.SqliteManager;
import com.imac.voice_app.util.dailyexercise.DailyExerciseActivity;
import com.imac.voice_app.util.history.HistoryActivity;
import com.imac.voice_app.util.setting.SettingActivity;
import com.imac.voice_app.util.speakSpeed.SpeakSpeedActivity;
import com.imac.voice_app.util.weeklyassessment.WeeklyAssessmentActivity;
import com.imac.voice_app.view.mainmenu.MainMenu;
import com.imac.voice_app.view.mainmenu.MenuClickListener;

import java.util.ArrayList;

import static com.imac.voice_app.util.login.LoginActivity.KEY_DAILY_EXERCISE;
import static com.imac.voice_app.util.login.LoginActivity.KEY_WEEKLY_EXERCISE;

public class MainActivity extends AppCompatActivity {

    private MainMenu mainMenu;
    private ArrayList<String> dailyTopicList;
    private ArrayList<String> weeklyTopicList;
    private ProgressDialog progressDialog;
    private Preferences preferences;
    private DataAppend dataAppend;
    private ArrayList<Integer> mPreferenceGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        preferences = new Preferences(this);
        dataAppend = new DataAppend();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.login_loading));
        progressDialog.setMessage(getString(R.string.login_wait));
        mainMenu = new MainMenu(this, onMenuClick());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainMenu.speedEnabler();
        addPreferenceValue();
//        mainMenu.weeklyAssessmentEnabler();
        show();
    }

    private void addPreferenceValue() {
        mPreferenceGroup = new ArrayList<>(6);
        mPreferenceGroup.add(preferences.getTopicOnePosition());
        mPreferenceGroup.add(preferences.getTopicTwoPosition());
        mPreferenceGroup.add(preferences.getTopicThreePosition());
        mPreferenceGroup.add(preferences.getTopicFourPosition());
        mPreferenceGroup.add(preferences.getTopicFivePosition());
        mPreferenceGroup.add(preferences.getTopicSixPosition());
    }

    private MenuClickListener onMenuClick() {
        return new MenuClickListener() {
            @Override
            public void onDailyExerciseClick() {
                if (0 == setDailyTopic().size())
                    Toast.makeText(MainActivity.this, "請設定每日練習題目", Toast.LENGTH_SHORT).show();
                else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(KEY_DAILY_EXERCISE, setDailyTopic());
                    ActivityLauncher.go(MainActivity.this, DailyExerciseActivity.class, bundle);
                }
            }

            @Override
            public void onWeeklyAssessmentClick() {
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_WEEKLY_EXERCISE, setWeeklyTopic());
                ActivityLauncher.go(MainActivity.this, WeeklyAssessmentActivity.class, bundle);
            }

            @Override
            public void onHistoryClick() {
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_WEEKLY_EXERCISE, weeklyTopicList);
                ActivityLauncher.go(MainActivity.this, HistoryActivity.class, bundle);
            }

            @Override
            public void onSpeakSpeedClick() {
                ActivityLauncher.go(MainActivity.this, SpeakSpeedActivity.class, null);
            }

            @Override
            public void onSettingClick() {
                ActivityLauncher.go(MainActivity.this, SettingActivity.class, null);
            }
        };
    }

    private void show() {
        SqliteManager manger = SqliteManager.getInstence(this);
        manger.getWeeklyTableALlSqlData();
    }

    private ArrayList<String> setDailyTopic() {
        ArrayList<String> result = new ArrayList<>(6);

        for (int i = 0; i < mPreferenceGroup.size(); i++)
            if (!mPreferenceGroup.get(i).equals(0)) result.add(String.valueOf(i));
        return result;
    }

    private ArrayList<String> setWeeklyTopic() {
        ArrayList<Boolean> status = dataAppend.formatBoolean(preferences.getWeeklyDoctorSetting());
        ArrayList<String> result = new ArrayList<>(7);
        for (int i = 0; i < status.size(); i++)
            if (status.get(i)) result.add(String.valueOf(i));
        return result;
    }

}
