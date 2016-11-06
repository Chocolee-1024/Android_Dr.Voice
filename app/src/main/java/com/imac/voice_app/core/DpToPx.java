package com.imac.voice_app.core;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DpToPx {
    private Context context;

    public DpToPx(Context context) {
        this.context = context;
    }

    public int dp(int dp) {

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float multiple = metrics.densityDpi / 160;
        return (int) (dp * multiple);
    }
}