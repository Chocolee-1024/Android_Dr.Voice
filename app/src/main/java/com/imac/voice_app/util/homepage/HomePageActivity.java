package com.imac.voice_app.util.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.imac.voice_app.R;
import com.imac.voice_app.core.ActivityLauncher;
import com.imac.voice_app.module.AlarmConstantManager;
import com.imac.voice_app.module.DataAppend;
import com.imac.voice_app.module.Preferences;
import com.imac.voice_app.util.login.LoginActivity;
import com.imac.voice_app.util.mainmenu.MainActivity;
import com.imac.voice_app.view.homepage.HomePage;

import java.util.Date;

import butterknife.ButterKnife;

/**
 * Created by isa on 2016/9/8.
 */
public class HomePageActivity extends AppCompatActivity {

    private String mode = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_layout);
        ButterKnife.bind(this);
        getBundle();
        init();
        checkPrefence();
    }

    private void getBundle() {
        Bundle whichMode = getIntent().getExtras();
        if (null != whichMode) {
            mode = whichMode.getString(AlarmConstantManager.INTENT_MODE, "");
        }
    }

    private void checkPrefence(){
        Preferences preferences =new Preferences(this);
        Date dateW =new Date(preferences.getWeeklyMillis());
        Date date1 =new Date(preferences.getDailyTimeOneMillis());
        Date date2 =new Date(preferences.getDailyTimeTwoMillis());
        Date date3 =new Date(preferences.getDailyTimeThreeMillis());
        Log.d("debug", "dateW: "+dateW);
        Log.d("debug", "date1: "+date1);
        Log.d("debug", "date2: "+date2);
        Log.d("debug", "date3: "+date3);
    }

    private void init() {
        HomePage homePage = new HomePage(this, new HomePage.OnClickEvent() {
            @Override
            public void onClick(boolean isLogin, String account, String name, String dailyExercise, String weeklyExercise) {
                if (!isLogin) {
                    ActivityLauncher.go(HomePageActivity.this, LoginActivity.class, null);
                } else {
                    DataAppend dataAppend = new DataAppend();
                    dataAppend.formatString(dailyExercise);
                    Bundle bundle = new Bundle();
                    bundle.putString(LoginActivity.KEY_LOGIN_ACCOUNT, account);
                    bundle.putString(LoginActivity.KEY_LOGIN_NAME, name);
                    ActivityLauncher.go(HomePageActivity.this, MainActivity.class, bundle);
                }
            }

        });

        if (!"".equals(mode)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (mode.equals(AlarmConstantManager.MODE_DAILY)) {
                AlertDialog alertDialog = builder.
                        setTitle("Voice 嗓音自我照護").
                        setMessage("開始做每日練習").
                        show();

            } else if (mode.equals(AlarmConstantManager.MODE_WEEK)) {
                AlertDialog alertDialog = builder.
                        setTitle("Voice 嗓音自我照護").
                        setMessage("開始做每週練習").
                        show();
            }
        }
    }

}
