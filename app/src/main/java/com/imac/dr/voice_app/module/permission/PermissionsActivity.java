package com.imac.dr.voice_app.module.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

@TargetApi(Build.VERSION_CODES.M)
public class PermissionsActivity extends AppCompatActivity {
    //權限接受
    public static final int PERMISSIONS_ACCEPT = 0;
    //權限拒絕
    public static final int PERMISSIONS_REFUSE = 1;
    //權限請求代碼
    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final String EXTRA_PERMISSIONS = "permission";

    private PermissionsChecker permissionsChecker;
    private boolean isRequireCheck;

    public static void startPermissionsForResult(Activity activity, int requestCode, String... permissions) {
        Log.e("note",permissions[0]+"....");
        //用來啟動這一個PermissionsActivity
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("note","onCreate");
        //判斷是否有用startPermissionsForResult來啟動這個PermissionsActivity
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {

            throw new RuntimeException("PermissionsActivity need to use a static method startPermissionsForResult to start!");
        }
        //new出PermissionsChecker
        permissionsChecker = new PermissionsChecker(this);
        isRequireCheck = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("note","onResume");
        if (isRequireCheck) {
            String[] permissions = getExtraPermissions();
            Log.e("noteeeeeee",permissions[0]);
            //判斷是否有權限
            if (permissionsChecker.missingPermissions(permissions)) {
                //"沒有"，要求權限
                requestPermissions(permissions);
                //"有"呼叫 allPermissionsAccept
            } else {
                allPermissionsAccept();
            }
        } else {
            isRequireCheck = true;
        }
    }
    //拿取"WRITE_EXTERNAL_STORAGE"這個權限
    private String[] getExtraPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }

    //跳出拒絕或允許的方法
    private void requestPermissions(String... permissions) {
        Log.e("note","requestPermissions");
        this.requestPermissions(permissions, PERMISSION_REQUEST_CODE);
    }
    /**
     * if the privilege is not missing then finish this page.
     * if the privilege is missing,prompted Dialog.
     *
     * @param requestCode  requestCode
     * @param permissions  permissions
     * @param grantResults grantResults
     */
    //當方框按下"允許"或"拒絕"
    /**如果有按下允許或拒絕 requestCode =  0 */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("note",String.valueOf(requestCode));
        //判斷是否有返回code，和呼叫isAllPermissionsAccept判斷是否有拒絕權限。
        if (requestCode == PERMISSION_REQUEST_CODE && isAllPermissionsAccept(grantResults)) {
            isRequireCheck = true;
            ///呼叫 allPermissionsAccept，關掉Activity。
            allPermissionsAccept();
        } else {
            isRequireCheck = false;
            //呼叫"showMissingPermissionDialog"顯示Dialog
             showMissingPermissionDialog();

        }
    }

    private boolean isAllPermissionsAccept(int[] grantResults) {
        for (int grantResult : grantResults) {
            //如果grantResults裡有拒絕存取(PERMISSION_DENIED)，就返回false
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
   //當按下拒絕時 ，跳出此Dialog
   private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsActivity.this);
        builder.setTitle("Help");
        builder.setMessage("Missed needing permissions ");
        //Quit按鈕監聽
        builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                //呼叫這個方法時，把資料傳回去()。
                setResult(PERMISSIONS_REFUSE);
                finish();
            }
        });
       //Settings按鈕監聽
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                //呼叫startSettings
                startSettings();
            }
        });
        builder.show();
    }

    private void startSettings() {
        //跳轉到"手機的設定"
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        //拿取App的PackageName
        Log.e("note",getPackageName(

        ));
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    private void allPermissionsAccept() {
        //呼叫這個方法時，把資料傳回去()。
        setResult(PERMISSIONS_ACCEPT);
        //關掉此PermissionsActivity
        finish();
    }
}