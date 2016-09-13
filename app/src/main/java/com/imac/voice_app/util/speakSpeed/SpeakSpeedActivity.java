package com.imac.voice_app.util.speakSpeed;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.imac.voice_app.R;
import com.imac.voice_app.module.CheckDate;
import com.imac.voice_app.module.SpeechKitModule;
import com.imac.voice_app.module.WriteToText;
import com.imac.voice_app.view.speakspeed.SpeakSpeedView;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class SpeakSpeedActivity extends Activity {
    private SpeakSpeedView layout;
    private SpeechKitModule mSpeechModule;
    private Context mContext;
    private Handler mHandlerTime;

    private int speechState;
    private int sec;
    private int secRecording;
    private int secCoolDown;
    private int count;
    private int calculateFont;
    private ArrayList<String> textLogArray;

    private static final int SEC_MAX = 60 * 50;
    private static final int SEC_RECORD = 30;
    private static final int SEC_COOL_DOWN = 30;
    private static final int COUNT_MAX = 16;

    //speechStatus status code
    private static final int STATUS_IDLE = 0;
    private static final int STATUS_NEED_START = 1;
    private static final int STATUS_RECORDING = 2;
    private static final int STATUS_COOL_DOWN = 3;
    private static final int STATUS_COUNT_MAX = 4;
    private static final int STATUS_NOT_USING = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_speed);

        //確認日期
//        CheckDate checkDate = new CheckDate(this);
//        checkDate.check();

        initSet();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void initSet() {
        layout = new SpeakSpeedView(this, callBackListener());
        mContext = this.getApplicationContext();

        mSpeechModule = new SpeechKitModule(mContext);
        mHandlerTime = new Handler();
        textLogArray = new ArrayList<>();

        calculateFont = 0;
        speechState = STATUS_NOT_USING;

        mSpeechModule.setTextUpdateListener(TextUpdateListener());
    }

    private void calculateNumPerMinute(int wordCount) {
        int percent = 0;
        int wordNum = 0;
        wordNum = wordCount / count * 2;
        percent = wordCount / 3;
        Log.e("wordCount",Integer.toString(wordCount));
        Log.e("percent",Integer.toString(percent));
        layout.setCalculateSpeedText(Integer.toString(wordNum), percent);
    }

    private void calculateUsedTime(int sec) {
        int cauMin = sec / 60;
        int cauSec = sec % 60;
        String strMin;
        String strSec;
        if (cauMin > 9) {
            strMin = Integer.toString(cauMin);
        } else {
            strMin = "0" + Integer.toString(cauMin);
        }

        if (cauSec > 9) {
            strSec = Integer.toString(cauSec);
        } else {
            strSec = "0" + Integer.toString(cauSec);
        }

        layout.setTimeTextViewText(strMin + ":" + strSec);
    }

    private void speakSpeedEnd() {
        //                    寫出
        WriteToText write = new WriteToText(this, textLogArray);
        write.write();
        speechState = STATUS_NOT_USING;
    }

    private final Runnable timerRun = new Runnable() {
        public void run() {
            ++sec;
            calculateUsedTime(sec);
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
                    speechState = STATUS_IDLE;
                    sec = 0;
                    secCoolDown = 0;
                    secRecording = 0;
                    calculateFont = 0;
                    mSpeechModule.startCaculateDB();
                    mHandlerTime.postDelayed(timerRun, 1000);
                    layout.setButtonStatus(true);
                } else {
                    if (speechState == STATUS_RECORDING) {
                        mSpeechModule.recognizeForceStop(true);
                    } else {
                        mSpeechModule.recognizeForceStop(false);
                    }
                    mHandlerTime.removeCallbacks(timerRun);
                    speechState = STATUS_NOT_USING;
                    layout.setButtonStatus(false);
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
}
