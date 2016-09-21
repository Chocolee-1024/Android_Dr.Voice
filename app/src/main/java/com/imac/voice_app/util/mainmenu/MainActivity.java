package com.imac.voice_app.util.mainmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.imac.voice_app.R;
import com.imac.voice_app.core.ActivityLauncher;
import com.imac.voice_app.util.login.LoginActivity;
import com.imac.voice_app.util.speakSpeed.SpeakSpeedActivity;
import com.imac.voice_app.view.mainmenu.MainMenu;

public class MainActivity extends AppCompatActivity {
    private String loginAccount;
    private String loginName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBundle();
        MainMenu mainMenu = new MainMenu(this, new MainMenu.OnClickEvent() {
            @Override
            public void onClick(View view, int position) {
                if (position == 3) {
                    Bundle bundle = new Bundle();
                    bundle.putString(LoginActivity.KEY_LOGIN_ACCOUNT, loginAccount);
                    bundle.putString(LoginActivity.KEY_LOGIN_NAME, loginName);
                    ActivityLauncher.go(MainActivity.this, SpeakSpeedActivity.class, bundle);
                }
            }
        });
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        loginAccount = bundle.getString(LoginActivity.KEY_LOGIN_ACCOUNT);
        loginName = bundle.getString(LoginActivity.KEY_LOGIN_NAME);
    }
}
