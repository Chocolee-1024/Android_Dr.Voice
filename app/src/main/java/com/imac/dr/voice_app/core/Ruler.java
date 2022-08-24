package com.imac.dr.voice_app.core;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Created by ameng on 9/8/16.
 */
public class Ruler {
    private int width;
    private int height;
    private DisplayMetrics displayMetrics;
    private Resources resources;

    public Ruler(Context context) {
        displayMetrics = new DisplayMetrics();
        //抓取螢幕大小
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        resources = context.getResources();
        //拿取手機寬
        width = displayMetrics.widthPixels;
        Log.e("TAG", String.valueOf(width));
        //拿取手機高
        height = displayMetrics.heightPixels;
        //拿取會顯示的頁面(包含標題)
        View decorView = ((Activity) context).getWindow().getDecorView();

        /**--------------(以被棄用)---------------
         * (NAVIGATION : 導航。 FULLSCREEN : 全屏。)
         * 0: NO FULLSCREEN and HAVE NAVIGATION
         * 2: NO FULLSCREEN and NO NAVIGATION
         * 6: HAVE FULLSCREEN and NO NAVIGATION
         * 4: HAVE FULLSCREEN and HAVE NAVIGATION
         * */
        int uiOptions = decorView.getSystemUiVisibility();
        //如果手機為橫的，之後再判斷 uiOptions。
        if (isHorizontalOrientation()) {
            if (uiOptions == 0) {
                height = height - getStatusBarHeight();
                width = width + getNavigationBarHeight();
            } else if (uiOptions == 2) {
                height = height - getStatusBarHeight();
            } else if (uiOptions == 6) {
                height = height;
                width = width + getNavigationBarHeight();
            } else if (uiOptions == 4) {
                height = height;
            }
        } else {
            if (uiOptions == 0) {
                height = height - getStatusBarHeight();
            } else if (uiOptions == 2) {
                height = height - getStatusBarHeight() + getNavigationBarHeight();
            } else if (uiOptions == 6) {
                height = height + getNavigationBarHeight();
            } else if (uiOptions == 4) {
                height = height;
            }
        }
    }

    public int getW(double Per) {
        if (Per == -1)
            return -1;
        else if (Per == -2)
            return -2;
        return (int) ((Per > 100.0) ? width : ((width * Per ) / 100));
    }

    public int getH(double Per) {
        if (Per == -1)
            return -1;
        else if (Per == -2)
            return -2;
        return (int) ((Per > 100.0) ? height : ((height * Per) / 100));
    }

    private int getNavigationBarHeight() {
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private int getStatusBarHeight() {
        //獲取資源檔id
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //return 狀態欄高度。
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private boolean isHorizontalOrientation() {
        Configuration config = resources.getConfiguration();
        //獲取系统屏幕的方向，判斷是否為橫向(ORIENTATION_LANDSCAPE)
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        }
        return false;
    }
}