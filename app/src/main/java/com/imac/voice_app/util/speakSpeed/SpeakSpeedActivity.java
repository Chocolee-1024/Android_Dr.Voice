package com.imac.voice_app.util.speakSpeed;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.imac.voice_app.R;
import com.imac.voice_app.component.ToolbarView;
import com.imac.voice_app.module.FileWriter;
import com.imac.voice_app.module.Preferences;
import com.imac.voice_app.module.SpeechKitModule;
import com.imac.voice_app.module.database.SqliteManager;
import com.imac.voice_app.module.net.DriveFile;
import com.imac.voice_app.module.permission.PermissionsActivity;
import com.imac.voice_app.module.permission.PermissionsChecker;
import com.imac.voice_app.view.speakspeed.SpeakSpeedView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class SpeakSpeedActivity extends Activity implements FileWriter.WriterCallBack {
    private SpeakSpeedView layout;
    private SpeechKitModule mSpeechModule;
    private PermissionsChecker permissionsChecker;
    private Context mContext;
    private Handler mHandlerTime;
    private FileWriter fileWriter;

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
    private Preferences preferences;
    private long startTimeMillin;
    private long endTimeMillin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_speed);
        mContext = this.getApplicationContext();
        fileWriter = new FileWriter(this);
        preferences = new Preferences(this);
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
        speechState = STATUS_NOT_USING;
//        DriveFile driveFile = new DriveFile(SpeakSpeedActivity.this, callbackEvent,FileUploader.FILE_VOICE_SPEED,preferences.getAccounnt());
//        driveFile.execute();
        fileWriter.setWriterCallBack(SpeakSpeedActivity.this);
        fileWriter.write(textLogArray);
        saveDataToDataBase();
    }

    private DriveFile.CallbackEvent callbackEvent = new DriveFile.CallbackEvent() {
        @Override
        public void onCallback() {
            fileWriter.setWriterCallBack(SpeakSpeedActivity.this);
            fileWriter.write(textLogArray);
        }

        @Override
        public void onFailCallback() {
            Toast.makeText(SpeakSpeedActivity.this, "存取失敗，請檢查網路", Toast.LENGTH_LONG).show();
        }
    };

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

    private void saveDataToDataBase() {
        Calendar calendar = Calendar.getInstance();
        endTimeMillin = calendar.getTimeInMillis();
        int recordTime = (int) ((endTimeMillin - startTimeMillin) / 1000);
        int speedCount = calculateSpeedCount(textLogArray);
        float speed = calculateSpeed(speedCount, recordTime);
        SqliteManager sqliteManager = SqliteManager.getInstence(this);
        sqliteManager.writeSpeedData(new String[]{
                String.valueOf(startTimeMillin)
                , String.valueOf(endTimeMillin)
                , String.valueOf(speedCount)
                , String.valueOf(speed)
                , String.valueOf(recordTime)
        });
    }

    private int calculateSpeedCount(ArrayList<String> data) {
        int result = 0;
        for (int i = 0; i < data.size(); i++) {
            result += data.get(i).length();
        }
        return result;
    }

    private int calculateSpeed(int speedCount, int recordTime) {
        return  Math.round(((float)speedCount / recordTime) * 60);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

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
                    layout.changeNotify(true);
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
                        Calendar calendar = Calendar.getInstance();
                        startTimeMillin = calendar.getTimeInMillis();
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
                    layout.changeNotify(false);
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
    public void onWriteSuccessful(File file) {
//        FileUploader uploader = new FileUploader(this,  loginAccount,FileUploader.FILE_VOICE_SPEED);
//        uploader.connect(file);
    }

    @Override
    public void onWriteFail() {
    }
}
