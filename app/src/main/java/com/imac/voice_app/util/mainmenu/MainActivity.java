package com.imac.voice_app.util.mainmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.imac.voice_app.R;
import com.imac.voice_app.core.ActivityLauncher;
import com.imac.voice_app.module.SqliteManger;
import com.imac.voice_app.util.dailyexercise.DailyExerciseActivity;
import com.imac.voice_app.util.login.LoginActivity;
import com.imac.voice_app.util.setting.SettingActivity;
import com.imac.voice_app.util.speakSpeed.SpeakSpeedActivity;
import com.imac.voice_app.util.weeklyassessment.WeeklyAssessmentActivity;
import com.imac.voice_app.view.mainmenu.MainMenu;
import com.imac.voice_app.view.mainmenu.MenuClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private MainMenu mainMenu;
    private String loginAccount;
    private String loginName;
    private ArrayList<String> topicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
        getBundle();
        mainMenu = new MainMenu(this, onMenuClick(), getIntent().getExtras());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainMenu.weeklyAssessmentEnabler();
        show();
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        loginAccount = bundle.getString(LoginActivity.KEY_LOGIN_ACCOUNT);
        loginName = bundle.getString(LoginActivity.KEY_LOGIN_NAME);
        topicList = (ArrayList<String>) bundle.getSerializable(LoginActivity.KEY_DAILY_EXERCISE);
    }

    private MenuClickListener onMenuClick() {
        return new MenuClickListener() {
            @Override
            public void onDailyExerciseClick() {
                Bundle bundle = new Bundle();
                bundle.putSerializable(LoginActivity.KEY_DAILY_EXERCISE, topicList);
                ActivityLauncher.go(MainActivity.this, DailyExerciseActivity.class, bundle);

            }

            @Override
            public void onWeeklyAssessmentClick() {
                ActivityLauncher.go(MainActivity.this, WeeklyAssessmentActivity.class, null);

            }

            @Override
            public void onHistoryClick() {

            }

            @Override
            public void onSpeakSpeedClick() {
                Bundle bundle = new Bundle();
                bundle.putString(LoginActivity.KEY_LOGIN_ACCOUNT, loginAccount);
                bundle.putString(LoginActivity.KEY_LOGIN_NAME, loginName);
                ActivityLauncher.go(MainActivity.this, SpeakSpeedActivity.class, bundle);
            }

            @Override
            public void onSettingClick() {
                ActivityLauncher.go(MainActivity.this, SettingActivity.class, null);
            }
        };
    }
//TODO 先show DataBase 資料
    private void show() {
        SqliteManger manger=new SqliteManger(this);
        manger.selectSoundPoint();
        manger.selectAssessmentPoint();
    }
}
