package com.imac.dr.voice_app.util.mainmenu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.core.ActivityLauncher;
import com.imac.dr.voice_app.module.DataAppend;
import com.imac.dr.voice_app.module.Preferences;
import com.imac.dr.voice_app.module.database.SqliteManager;
import com.imac.dr.voice_app.util.dailyexercise.DailyExerciseActivity;
import com.imac.dr.voice_app.util.healtheducation.HealthEducationActivity;
import com.imac.dr.voice_app.util.history.HistoryActivity;
import com.imac.dr.voice_app.util.setting.SettingActivity;
import com.imac.dr.voice_app.util.speakSpeed.SpeakSpeedActivity;
import com.imac.dr.voice_app.util.weeklyassessment.WeeklyAssessmentActivity;
import com.imac.dr.voice_app.view.mainmenu.MainMenu;
import com.imac.dr.voice_app.view.mainmenu.MenuClickListener;

import java.util.ArrayList;

import static com.imac.dr.voice_app.util.login.LoginActivity.KEY_DAILY_EXERCISE;
import static com.imac.dr.voice_app.util.login.LoginActivity.KEY_WEEKLY_EXERCISE;

public class MainActivity extends AppCompatActivity {

    private MainMenu mainMenu;
    private ArrayList<String> dailyTopicList;
    private ArrayList<String> weeklyTopicList;
    private ProgressDialog progressDialog;
    private Preferences preferences;
    private DataAppend dataAppend;
    private ArrayList<Integer> mPreferenceGroup;
    private Dialog messageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        preferences = new Preferences(this);
        dataAppend = new DataAppend();
        //--------------new ProgressDialog--------------
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.login_loading));
        progressDialog.setMessage(getString(R.string.login_wait));
        //==============================================
        //-------------------------醫師設定沒有設定值時顯示的Dialog-------------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        messageDialog = builder
                .setTitle(R.string.main_menu_dialog_title)
                .setMessage(R.string.main_menu_dialog_message)
                .setPositiveButton(R.string.main_menu_dialog_positivebutton_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        //==========================================================================
        mainMenu = new MainMenu(this, onMenuClick());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //呼叫speedEnabler
        mainMenu.speedEnabler();
        //呼叫addPreferenceValue
        addPreferenceValue();
        //呼叫weeklyAssessmentEnabler
        mainMenu.weeklyAssessmentEnabler();
        //呼叫show
        show();
    }
    //拿取醫生設定每日練習的數值。
    private void addPreferenceValue() {
        //每一個Topic都有(0,1,3,5)的數值，共有6個Topic(放鬆，呼吸，喝水，高音，共鳴)
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
                //判斷醫生設定每日練習有沒有設定過值，沒有就跳出Dialog。
                if (0 == setDailyTopic().size()) {
                    messageDialog.show();

                } else {
                    //跳到每日練習頁面，並把Bundle丟入
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(KEY_DAILY_EXERCISE, setDailyTopic());
                    ActivityLauncher.go(MainActivity.this, DailyExerciseActivity.class, bundle);
                }
            }
            @Override
            public void onWeeklyAssessmentClick() {
                //判斷醫生設定每周練習有沒有設定過值，沒有就跳出Dialog。
                if (0 == setWeeklyTopic().size()) {
                    messageDialog.show();
                } else {
                    //跳到每周練習頁面，並把Bundle丟入
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(KEY_WEEKLY_EXERCISE, setWeeklyTopic());
                    ActivityLauncher.go(MainActivity.this, WeeklyAssessmentActivity.class, bundle);
                }
            }

            @Override
            public void onHistoryClick() {
                //跳到歷史紀錄頁面，並把Bundle丟入(weeklyTopicList為null)
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_WEEKLY_EXERCISE, weeklyTopicList);
                ActivityLauncher.go(MainActivity.this, HistoryActivity.class, bundle);
            }

            @Override
            public void onSpeakSpeedClick() {
                //跳到語速設定頁面
                ActivityLauncher.go(MainActivity.this, SpeakSpeedActivity.class, null);
            }

            @Override
            public void onSettingClick() {
                //跳到設定頁面
                ActivityLauncher.go(MainActivity.this, SettingActivity.class, null);
            }

            @Override
            public void onEductionClick() {
                //跳到噪音衛教頁面
                ActivityLauncher.go(MainActivity.this, HealthEducationActivity.class, null);
            }
        };
    }

    private void show() {
        //建立SqliteManager
        SqliteManager manger = SqliteManager.getInstence(this);
        //呼叫getWeeklyTableALlSqlData
        manger.getWeeklyTableALlSqlData();
    }
    //拿取每日練習的值
    private ArrayList<String> setDailyTopic() {
        ArrayList<String> result = new ArrayList<>(6);
        //判斷如果(mPreferenceGroup.get(i))的值不等於零，就放入Topic編號
        for (int i = 0; i < mPreferenceGroup.size(); i++)
            if (!mPreferenceGroup.get(i).equals(0)) result.add(String.valueOf(i));
        return result;
    }
    //拿取自我評估的值(每周練習)
    private ArrayList<String> setWeeklyTopic() {
        //拿取值放入"status"
        ArrayList<Boolean> status = dataAppend.formatBoolean(preferences.getWeeklyDoctorSetting());
        ArrayList<String> result = new ArrayList<>(7);
        //如果status裡的值是"true"，放入該Topic的編號
        for (int i = 0; i < status.size(); i++)
            if (status.get(i)) result.add(String.valueOf(i));
        return result;
    }

}
