package com.imac.voice_app.util.mainmenu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.imac.voice_app.R;
import com.imac.voice_app.core.ActivityLauncher;
import com.imac.voice_app.module.database.SqliteManager;
import com.imac.voice_app.module.net.SearchName;
import com.imac.voice_app.util.dailyexercise.DailyExerciseActivity;
import com.imac.voice_app.util.history.HistoryActivity;
import com.imac.voice_app.util.login.LoginActivity;
import com.imac.voice_app.util.setting.SettingActivity;
import com.imac.voice_app.util.speakSpeed.SpeakSpeedActivity;
import com.imac.voice_app.util.weeklyassessment.WeeklyAssessmentActivity;
import com.imac.voice_app.view.mainmenu.MainMenu;
import com.imac.voice_app.view.mainmenu.MenuClickListener;

import java.util.ArrayList;

import static com.imac.voice_app.util.login.LoginActivity.KEY_DAILY_EXERCISE;
import static com.imac.voice_app.util.login.LoginActivity.KEY_SETTING;
import static com.imac.voice_app.util.login.LoginActivity.KEY_WEEKLY_EXERCISE;

public class MainActivity extends AppCompatActivity {

    private MainMenu mainMenu;
    private String loginAccount;
    private String loginName;
    private ArrayList<String> dailyTopicList;
    private ArrayList<String> weeklyTopicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getBundle();
        mainMenu = new MainMenu(this, onMenuClick(), getIntent().getExtras());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO 暫時關閉 weekly dismiss 功能
//        mainMenu.weeklyAssessmentEnabler();
        show();
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        loginAccount = bundle.getString(LoginActivity.KEY_LOGIN_ACCOUNT);
        loginName = bundle.getString(LoginActivity.KEY_LOGIN_NAME);
    }

    private MenuClickListener onMenuClick() {
        return new MenuClickListener() {
            @Override
            public void onDailyExerciseClick() {
                searchDailyTopic();
            }

            @Override
            public void onWeeklyAssessmentClick() {
                searchWeeklyTopic();
            }

            @Override
            public void onHistoryClick() {
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_WEEKLY_EXERCISE, weeklyTopicList);
                ActivityLauncher.go(MainActivity.this, HistoryActivity.class, bundle);
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
                getRemindTime();
            }
        };
    }

    //TODO 先show DataBase 資料
    private void show() {
        SqliteManager manger = SqliteManager.getIntence(this);
        manger.getALlSqlData();
    }

    private void searchDailyTopic() {
        final ProgressDialog   progressDialog = ProgressDialog.show(MainActivity.this,
                getString(R.string.login_loading),
                getString(R.string.login_wait),
                true
        );
        String fileName = "每日練習" + loginAccount;
        SearchName searchDailyTopic = new SearchName(MainActivity.this, fileName, new SearchName.onCallBackEvent() {
            @Override
            public void onSearchResult(ArrayList<String> search) {
                Bundle bundle=new Bundle();
                bundle.putSerializable(KEY_DAILY_EXERCISE, search);
                ActivityLauncher.go(MainActivity.this, DailyExerciseActivity.class, bundle);
                progressDialog.dismiss();
            }

            @Override
            public void onSearchFail() {
                Toast.makeText(MainActivity.this, "請檢查網路或檔案是否存在", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        searchDailyTopic.execute();
    }

    private void searchWeeklyTopic() {
        final ProgressDialog   progressDialog = ProgressDialog.show(MainActivity.this,
                getString(R.string.login_loading),
                getString(R.string.login_wait),
                true
        );
        String fileName = "每週用聲紀錄" + loginAccount;
        SearchName searchDailyTopic = new SearchName(MainActivity.this, fileName, new SearchName.onCallBackEvent() {
            @Override
            public void onSearchResult(ArrayList<String> search) {
                Bundle bundle=new Bundle();
                bundle.putSerializable(KEY_WEEKLY_EXERCISE, search);
                ActivityLauncher.go(MainActivity.this, WeeklyAssessmentActivity.class, bundle);
                progressDialog.dismiss();
            }

            @Override
            public void onSearchFail() {
                Toast.makeText(MainActivity.this, "請檢查網路或檔案是否存在", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        searchDailyTopic.execute();
    }

    private void getRemindTime() {
        final ProgressDialog   progressDialog = ProgressDialog.show(MainActivity.this,
                getString(R.string.login_loading),
                getString(R.string.login_wait),
                true
        );
        String fileName ="提醒時間" + loginAccount;
        SearchName searchName = new SearchName(MainActivity.this, fileName, new SearchName.onCallBackEvent() {
            @Override
            public void onSearchResult(ArrayList<String> search) {
                Bundle bundle=new Bundle();
                bundle.putSerializable(KEY_SETTING, search);
                ActivityLauncher.go(MainActivity.this, SettingActivity.class, bundle);
                progressDialog.dismiss();
            }

            @Override
            public void onSearchFail() {
                Toast.makeText(MainActivity.this, "請檢查網路或檔案是否存在", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        searchName.execute();
    }
}
