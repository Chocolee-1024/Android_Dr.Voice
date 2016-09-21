package com.imac.voice_app.util.speakSpeed;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.imac.voice_app.R;
import com.imac.voice_app.component.ToolbarView;
import com.imac.voice_app.module.FileUploader;
import com.imac.voice_app.module.FileWriter;
import com.imac.voice_app.module.PermissionsActivity;
import com.imac.voice_app.module.PermissionsChecker;
import com.imac.voice_app.module.SpeechKitModule;
import com.imac.voice_app.module.base.BaseGoogleDrive;
import com.imac.voice_app.util.login.LoginActivity;
import com.imac.voice_app.util.mainmenu.MainActivity;
import com.imac.voice_app.view.speakspeed.SpeakSpeedView;

import java.util.ArrayList;
import java.util.Date;


public class SpeakSpeedActivity extends Activity {
    private SpeakSpeedView layout;
    private SpeechKitModule mSpeechModule;
    private PermissionsChecker permissionsChecker;
    private Context mContext;
    private Handler mHandlerTime;
    private FileWriter fileWriter;
    private String loginAccount;
    private String loginName;

    private int speechState;
    private int sec;
    private int secRecording;
    private int secCoolDown;
    private int count;
    private int calculateFont;
    private ArrayList<String> textLogArray;

    private static final int ASK_PERMISSION_CODE = 0;
//    private static final int SEC_MAX = 60 * 50;
//    private static final int SEC_RECORD = 30;
//    private static final int SEC_COOL_DOWN = 30;
//    private static final int COUNT_MAX = 16;

    //test build setting
    private static final int SEC_MAX = 60 * 5;
    private static final int SEC_RECORD = 15;
    private static final int SEC_COOL_DOWN = 3;
    private static final int COUNT_MAX = 8;

    //speechStatus status code
    private static final int STATUS_IDLE = 0;
    private static final int STATUS_NEED_START = 1;
    private static final int STATUS_RECORDING = 2;
    private static final int STATUS_COOL_DOWN = 3;
    private static final int STATUS_COUNT_MAX = 4;
    private static final int STATUS_NOT_USING = 5;

    private String[] checkPermission = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_speed);
        mContext = this.getApplicationContext();
        fileWriter = new FileWriter(this);
        initSet();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void initSet() {
        layout = new SpeakSpeedView(this, callBackListener());
        mSpeechModule = new SpeechKitModule(mContext);
        mHandlerTime = new Handler();
        textLogArray = new ArrayList<>();
        permissionsChecker = new PermissionsChecker(this);

        calculateFont = 0;
        speechState = STATUS_NOT_USING;

        mSpeechModule.setTextUpdateListener(TextUpdateListener());
        layout.setToolbarViewCallBack(toolbarCallBack());
        getBundle();
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        loginAccount = bundle.getString(LoginActivity.KEY_LOGIN_ACCOUNT);
        loginName = bundle.getString(LoginActivity.KEY_LOGIN_NAME);
    }

    private void calculateNumPerMinute(int wordCount) {
        int wordNum = wordCount * 4;
        int percent = wordNum / 3;
        Log.e("wordCount", Integer.toString(wordCount));
        Log.e("percent", Integer.toString(percent));
        layout.setCalculateSpeedText(Integer.toString(wordNum), percent);
    }


//    TODO：需要討論介面
//    private void calculateUsedTime(int sec) {
//        int cauMin = sec / 60;
//        int cauSec = sec % 60;
//        String strMin;
//        String strSec;
//        if (cauMin > 10) {
//            strMin = Integer.toString(cauMin);
//        } else {
//            strMin = "0" + Integer.toString(cauMin);
//        }
//
//        if (cauSec > 10) {
//            strSec = Integer.toString(cauSec);
//        } else {
//            strSec = "0" + Integer.toString(cauSec);
//        }
//
//        layout.setTimeTextViewText(strMin + ":" + strSec);
//    }

    private void speakSpeedEnd() {
        //                    寫出
        FileUploader uploader = new FileUploader(this,loginName,loginAccount);
        fileWriter.write(loginName, textLogArray);
        uploader.connect(fileWriter.getFile());
        speechState = STATUS_NOT_USING;

    }

    private final Runnable timerRun = new Runnable() {
        public void run() {
            ++sec;
//            calculateUsedTime(sec);
            switch (speechState) {
                case STATUS_NEED_START:
                    mSpeechModule.recognizeStart();
                    speechState = STATUS_RECORDING;
                    break;
                case STATUS_RECORDING:
                    ++secRecording;
                    Log.e("secRecording", secRecording + "");
                    break;
                case STATUS_COOL_DOWN:
                    ++secCoolDown;
                    Log.e("secCoolDown", secCoolDown + "");
                    break;
            }

            if (secCoolDown == SEC_COOL_DOWN) {
                speechState = STATUS_IDLE;
                secCoolDown = 0;
                mSpeechModule.startCaculateDB();
                Log.e("secCoolDown", "secCoolDown end");
            }

            if (secRecording == SEC_RECORD) {
                mSpeechModule.recognizeStop();
                speechState = STATUS_COOL_DOWN;
                secRecording = 0;
                ++count;
            }

            if (count == COUNT_MAX) {
                speechState = STATUS_COUNT_MAX;
            }

            if (sec == SEC_MAX) {
                speakSpeedEnd();
            } else {
                mHandlerTime.postDelayed(this, 1000);
            }
        }
    };

    private SpeechKitModule.onTextUpdateListener TextUpdateListener() {
        return new SpeechKitModule.onTextUpdateListener() {
            @Override
            public void updateText(String str) {
                textLogArray.add(str);
                calculateFont = calculateFont + str.length();
                calculateNumPerMinute(calculateFont);
            }

            @Override
            public void ErrorOccurred() {
                textLogArray.add("");
                calculateNumPerMinute(calculateFont);
            }

            @Override
            public void UserTalking() {
                if (speechState == STATUS_IDLE) {
                    speechState = STATUS_NEED_START;
                }
            }
        };
    }

    private SpeakSpeedView.callBackListener callBackListener() {
        return new SpeakSpeedView.callBackListener() {
            @Override
            public void checkButton() {
                if (speechState == STATUS_NOT_USING) {
                    if (permissionsChecker.missingPermissions(checkPermission)) {
                        PermissionsActivity.startPermissionsForResult(SpeakSpeedActivity.this,
                                ASK_PERMISSION_CODE,
                                checkPermission);
                    } else {
                        speechState = STATUS_IDLE;
                        sec = 0;
                        secCoolDown = 0;
                        secRecording = 0;
                        calculateFont = 0;
                        mSpeechModule.startCaculateDB();
                        mHandlerTime.postDelayed(timerRun, 1000);
                        layout.setButtonStatus(true);
                        layout.setmStatusHintText("");
                        layout.setStartTextViewVisibility(false);
                        Date date = new Date();
                        fileWriter.setStartTime(date);
                    }
                } else {
                    if (speechState == STATUS_RECORDING) {
                        mSpeechModule.recognizeForceStop(true);
                    } else {
                        mSpeechModule.recognizeForceStop(false);
                    }
                    mHandlerTime.removeCallbacks(timerRun);
                    speechState = STATUS_NOT_USING;
                    layout.setButtonStatus(false);
                    layout.stopButtonResetView();
                    layout.setStartTextViewVisibility(true);
                    speakSpeedEnd();
                }
            }

            @Override
            public void closeButton() {
                if (speechState == STATUS_RECORDING) {
                    mSpeechModule.recognizeForceStop(true);
                } else {
                    mSpeechModule.recognizeForceStop(false);
                }
                mHandlerTime.removeCallbacks(timerRun);
                speechState = STATUS_NOT_USING;
                SpeakSpeedActivity.this.finish();
            }
        };
    }

    private ToolbarView.toolbarCallBack toolbarCallBack(){
        return new ToolbarView.toolbarCallBack() {
            @Override
            public void backButtonListener() {

            }

            @Override
            public void menuButtonListener() {
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_zoom_in_top,R.anim.anim_zoom_out_top);

                finish();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseGoogleDrive.ASK_ACCOUNT && resultCode == RESULT_OK) {
            String account = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            FileUploader uploader = new FileUploader(this,loginName,loginAccount);
            if (account.equals(FileUploader.ACCOUNT_NAME))
                uploader.getCredential().setSelectedAccountName(account);
            uploader.connect(fileWriter.getFile());
        }
    }
}
