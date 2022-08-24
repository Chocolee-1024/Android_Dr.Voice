package com.imac.dr.voice_app.module.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public class PermissionsChecker {
    private Context context;

    public PermissionsChecker(Context context) {
        this.context = context;
    }

    /**
     * check every input permissions
     *
     * @param permissions needs permissions
     * @return if this application lose permissions return true else return false
     */
    public boolean missingPermissions(String... permissions) {
        //判斷api版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                //判斷是否有開權限---------------------------------------------------(沒有權限)
                if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED)
                    return true;
            }
        }
        return false;
    }
}