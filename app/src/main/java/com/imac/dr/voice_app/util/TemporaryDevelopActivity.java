package com.imac.dr.voice_app.util;

import android.os.Bundle;

import com.imac.dr.voice_app.core.SeparateDeveloperActivity;
import com.imac.dr.voice_app.util.homepage.HomePageActivity;
import com.imac.dr.voice_app.util.login.LoginActivity;
import com.imac.dr.voice_app.util.setting.SettingActivity;
import com.imac.dr.voice_app.util.speakSpeed.SpeakSpeedActivity;
import com.imac.dr.voice_app.util.mainmenu.MainActivity;

public class TemporaryDevelopActivity extends SeparateDeveloperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addActivityButton(HomePageActivity.class,null);
        addActivityButton(LoginActivity.class,null);
        addActivityButton(SpeakSpeedActivity.class,null);
        addActivityButton(MainActivity.class,null);
        addActivityButton(SettingActivity.class,null);
    }
}