package com.imac.dr.voice_app.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ActivityLauncher {
    //換頁功能。
    public static void go(Context context, Class<? extends Activity> activityClass, Bundle args) {
        Intent intent = new Intent(context, activityClass);
        //如果有傳入Bundle才做。
        if (args != null) {
            intent.putExtras(args);
        }
        context.startActivity(intent);
    }
}