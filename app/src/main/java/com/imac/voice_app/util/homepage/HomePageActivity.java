package com.imac.voice_app.util.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.imac.voice_app.R;
import com.imac.voice_app.core.ActivityLauncher;
import com.imac.voice_app.module.DataAppend;
import com.imac.voice_app.util.login.LoginActivity;
import com.imac.voice_app.util.mainmenu.MainActivity;
import com.imac.voice_app.view.homepage.HomePage;

import butterknife.ButterKnife;

/**
 * Created by isa on 2016/9/8.
 */
public class HomePageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_layout);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        HomePage homePage = new HomePage(this, new HomePage.OnClickEvent() {
            @Override
            public void onClick(boolean isLogin, String account, String name, String dailyExercise) {
                if (!isLogin) {
                    ActivityLauncher.go(HomePageActivity.this, LoginActivity.class, null);
                } else {
                    DataAppend dataAppend = new DataAppend();
                    dataAppend.split(dailyExercise);
                    Bundle bundle = new Bundle();
                    bundle.putString(LoginActivity.KEY_LOGIN_ACCOUNT, account);
                    bundle.putString(LoginActivity.KEY_LOGIN_NAME, name);
                    bundle.putSerializable(LoginActivity.KEY_DAILY_EXERCISE, dataAppend.split(dailyExercise));
                    ActivityLauncher.go(HomePageActivity.this, MainActivity.class, bundle);
                }
            }

        });
    }

}
