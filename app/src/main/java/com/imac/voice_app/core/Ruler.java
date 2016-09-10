package com.imac.voice_app.core;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
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
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        resources = context.getResources();
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        View decorView = ((Activity) context).getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        /**
         * 0: NO FULLSCREEN and HAVE NAVIGATION
         * 2: NO FULLSCREEN and NO NAVIGATION
         * 6: HAVE FULLSCREEN and NO NAVIGATION
         * 4: HAVE FULLSCREEN and HAVE NAVIGATION
         * */
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
        return (int) ((Per > 100.0) ? width : ((width * Per) / 100));
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
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private boolean isHorizontalOrientation() {
        Configuration config = resources.getConfiguration();
        //Horizontal screen
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        }
        return false;
    }
}