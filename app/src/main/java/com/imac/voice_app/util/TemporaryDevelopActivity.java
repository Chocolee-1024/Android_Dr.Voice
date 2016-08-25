package com.imac.voice_app.util;

import android.os.Bundle;

import com.imac.voice_app.core.SeparateDeveloperActivity;
import com.imac.voice_app.util.login.LoginActivity;

public class TemporaryDevelopActivity extends SeparateDeveloperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivityButton(LoginActivity.class,null);
    }
}