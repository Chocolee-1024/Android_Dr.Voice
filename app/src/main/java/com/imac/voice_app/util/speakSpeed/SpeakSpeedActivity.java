package com.imac.voice_app.util.speakSpeed;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;

import com.imac.voice_app.R;
import com.imac.voice_app.component.ToolbarView;
import com.imac.voice_app.module.net.FileUploader;
import com.imac.voice_app.module.FileWriter;
import com.imac.voice_app.module.permission.PermissionsActivity;
import com.imac.voice_app.module.permission.PermissionsChecker;
import com.imac.voice_app.module.SpeechKitModule;
import com.imac.voice_app.module.net.base.BaseGoogleDrive;
import com.imac.voice_app.util.login.LoginActivity;
import com.imac.voice_app.view.speakspeed.SpeakSpeedView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;


public class SpeakSpeedActivity extends Activity implements FileWriter.WriterCallBack {
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
    private int count;
    private int calculateFont;
    private ArrayList<String> textLogArray;

    private static final int ASK_PERMISSION_CODE = 0;

    private static final int SEC_MAX = 60 * 50;
    private static final int SEC_RECORD = 30;
    private static final int COUNT_MAX = 16;

    //speechStatus status code
    private static final int STATUS_IDLE = 0;
    private static final int STATUS_NEED_START = 1;
    private static final int STATUS_RECORDING = 2;
    private static final int STATUS_COUNT_MAX = 4;
    private static final int STATUS_NOT_USING = 5;

    private String[] checkPermission = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.VIBRATE};

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
        int wordNum = wordCount * 2;
        int percent = wordNum / 3;
        Log.e("wordCount", Integer.toString(wordCount));
        Log.e("percent", Integer.toString(percent));
        layout.setCalculateSpeedText(wordNum, percent);
        if (wordNum > 200) {
            setVibrator(true);
        } else if (wordNum > 160) {
            setVibrator(false);
        }

    }

    private void speakSpeedEnd() {
        //                    寫出
        fileWriter.setWriterCallBack(this);
        fileWriter.write(loginName, textLogArray);

        speechState = STATUS_NOT_USING;

    }

    private void setVibrator(boolean level) {
        Vibrator myVibrator = (Vibrator) getApplication().getSystemService(VIBRATOR_SERVICE);
        if (!level) {
            myVibrator.vibrate(100);
        } else {
            myVibrator.vibrate(new long[]{50, 100, 50, 100}, -1);
        }
    }

    private final Runnable timerRun = new Runnable() {
        public void run() {
            ++sec;
            switch (speechState) {
                case STATUS_NEED_START:
                    mSpeechModule.recognizeStart();
                    speechState = STATUS_RECORDING;
                    break;
                case STATUS_RECORDING:
                    ++secRecording;
                    Log.e("secRecording", secRecording + "");
                    break;
            }

            if (secRecording == SEC_RECORD) {
                mSpeechModule.recognizeStop();
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

    /************
     * Callback Func
     ***********/
    private SpeechKitModule.onTextUpdateListener TextUpdateListener() {
        return new SpeechKitModule.onTextUpdateListener() {
            @Override
            public void updateText(String str) {
                textLogArray.add(str);
                calculateFont = calculateFont + str.length();
                calculateNumPerMinute(str.length());
                speechState = STATUS_IDLE;
                secRecording = 0;
                mSpeechModule.startCaculateDB();
            }

            @Override
            public void ErrorOccurred() {
                textLogArray.add("");
                calculateNumPerMinute(0);
                speechState = STATUS_IDLE;
                secRecording = 0;
                mSpeechModule.startCaculateDB();
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

    private ToolbarView.toolbarCallBack toolbarCallBack() {
        return new ToolbarView.toolbarCallBack() {
            @Override
            public void backButtonListener() {

            }

            @Override
            public void menuButtonListener() {
                finish();
                overridePendingTransition(R.anim.anim_zoom_in_top, R.anim.anim_zoom_out_top);

                finish();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseGoogleDrive.ASK_ACCOUNT && resultCode == RESULT_OK) {
            String account = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            FileUploader uploader = new FileUploader(this, loginName, loginAccount);
            if (account.equals(FileUploader.ACCOUNT_NAME))
                uploader.getCredential().setSelectedAccountName(account);
            uploader.connect(fileWriter.getFile());
        }
    }

    @Override
    public void onWriteSuccessful(File file) {
//        FileUploader uploader = new FileUploader(this, loginName, loginAccount);
//        uploader.connect(file);
    };

    @Override
    public void onWriteFail() {

    }
}
