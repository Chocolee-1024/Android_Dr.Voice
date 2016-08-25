package com.imac.voice_app.util.speakSpeed;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.imac.voice_app.R;
import com.imac.voice_app.module.CheckDate;
import com.imac.voice_app.module.SpeechKitModule;
import com.imac.voice_app.module.WriteToText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpeakSpeedActivity extends Activity {
    @BindView(R.id.ed_name)
    EditText mNameText;
    @BindView(R.id.tv_status_text)
    TextView mStatusText;
    @BindView(R.id.tv_font_number_calculate)
    TextView mCalculateFontNumber;
    @BindView(R.id.btn_Check)
    Button mCheckButton;
    @BindView(R.id.btn_back)
    Button mBackButton;
    @BindView(R.id.rg_sex)
    RadioGroup mSexGroup;

    private SpeechKitModule mSpeechModule;
    private Context mContext;
    private Handler mHandlerTime;

    private int speechState;
    private int sec;
    private int secRecording;
    private int secCoolDown;
    private int count;
    private int calculateFont;
    private String sex;
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
        ButterKnife.bind(this);


        //確認日期
        CheckDate checkDate = new CheckDate(this);
        checkDate.check();

        initSet();
        devEnvironment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void initSet() {
        mContext = this.getApplicationContext();

        mSpeechModule = new SpeechKitModule(mContext);
        mHandlerTime = new Handler();
        textLogArray = new ArrayList<>();

        calculateFont = 0;
        speechState = STATUS_NOT_USING;
        mCalculateFontNumber.setText(String.valueOf(calculateFont));

        mSexGroup.setOnCheckedChangeListener(RadioChangeListener());
        mSpeechModule.setTextUpdateListener(TextUpdateListener());
    }

    private void devEnvironment() {
        mNameText.setText(R.string.name);
        mSexGroup.check(R.id.rb_boy);
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
            Log.e("sec", sec + "");
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

    @OnClick({R.id.btn_back, R.id.btn_Check})
    public void buttonListener(Button button) {
        switch (button.getId()) {
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_Check:
                if (speechState == STATUS_NOT_USING) {
                    if (TextUtils.isEmpty(mNameText.getText().toString())) {
                        Toast.makeText(mContext, "Please enter your name", Toast.LENGTH_SHORT)
                                .show();
                    } else if (TextUtils.isEmpty(sex)) {
                        Toast.makeText(mContext, "Please enter your sex", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        speechState = STATUS_IDLE;
                        sec = 0;
                        secCoolDown = 0;
                        secRecording = 0;
                        calculateFont = 0;
                        mSpeechModule.startCaculateDB();
                        mHandlerTime.postDelayed(timerRun, 1000);
                    }
                }
                break;
        }
    }

    private RadioGroup.OnCheckedChangeListener RadioChangeListener() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_boy:
                        sex = "男";
                        break;
                    case R.id.rb_girl:
                        sex = "女";
                        break;
                }
            }
        };
    }

    private SpeechKitModule.onTextUpdateListener TextUpdateListener() {
        return new SpeechKitModule.onTextUpdateListener() {
            @Override
            public void updateText(String str) {
                textLogArray.add(str);
                calculateFont = calculateFont + str.length();
                mCalculateFontNumber.setText(String.valueOf(calculateFont));
            }

            @Override
            public void updateLog(String log) {
                mStatusText.setText(log);
            }

            @Override
            public void ErrorOccurred() {
                textLogArray.add("");
            }

            @Override
            public void UserTalking() {
                if (speechState == STATUS_IDLE) {
                    speechState = STATUS_NEED_START;
                }
            }
        };
    }

    public String getName() {
        return mNameText.getText().toString();
    }

    public String getSex() {
        return sex.equals("男") ? "0" : "1";
    }
}
