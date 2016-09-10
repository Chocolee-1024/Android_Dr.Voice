package com.imac.voice_app.module;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.nuance.speechkit.DetectionType;
import com.nuance.speechkit.Language;
import com.nuance.speechkit.Recognition;
import com.nuance.speechkit.RecognitionType;
import com.nuance.speechkit.Session;
import com.nuance.speechkit.Transaction;
import com.nuance.speechkit.TransactionException;


/**
 * Speech kit Module used to Nuance's ASR feature
 */
public class SpeechKitModule {
    //至官方網站申請帳號取得開發用授權如下列String與Uri
    public static final String APP_KEY = "d826a3aae3a5193336c77888738fbcc10cc40e0011cefbeaf6b193827298784abe1bf5a1c00ed4f6d64d7e49dc3a0ae9ff18e910e6e4fadc958d5f0826d752e4";
    public static final String APP_ID = "NMDPTRIAL_u07f08_gmail_com20160621234119";
    public static final String SERVER_HOST = "sslsandbox.nmdp.nuancemobility.net";
    public static final String SERVER_PORT = "443";
    public static final Uri SERVER_URI = Uri.parse("nmsps://" + APP_ID + "@" + SERVER_HOST + ":" + SERVER_PORT);

    private static final String CHINESE_CODE = "cmn-TWN";
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_DEFAULT;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private Transaction transaction;
    private Transaction.Options options;
    private Session session;

    private AudioRecord recorder = null;
    private Handler mHandlerTime;
    private int bufferSize = 0;
    private boolean forceStop;

    public interface onTextUpdateListener {
        void updateText(String str);

        void ErrorOccurred();

        void UserTalking();
    }

    private onTextUpdateListener mTextUpdateListener = null;

    public SpeechKitModule(Context context) {
        session = Session.Factory.session(context, SERVER_URI, APP_KEY);
        options = new Transaction.Options();
        mHandlerTime = new Handler();
        forceStop = false;
    }

    private Transaction.Listener SpeechListener() {
        return new Transaction.Listener() {
            @Override
            public void onStartedRecording(Transaction transaction) {
                if (!forceStop) {
                    Log.d("SpeechKitModule", "Started Recording");
                }
            }

            @Override
            public void onFinishedRecording(Transaction transaction) {
                if (!forceStop) {
                    Log.d("SpeechKitModule", "Finished Recording");
                }
            }

            @Override
            public void onRecognition(Transaction transaction, Recognition recognition) {
                if (!forceStop) {
                    Log.d("SpeechKitModule", "Recognize");
                    mTextUpdateListener.updateText(recognition.getText());
                }
            }

            @Override
            public void onSuccess(Transaction transaction, String suggestion) {
                if (!forceStop) {
                    Log.d("SpeechKitModule", "All Success");
                }
            }

            @Override
            public void onError(Transaction transaction, String suggestion, TransactionException e) {
                if (!forceStop) {
                    Log.d("SpeechKitModule", "Recognizing Fail，" + suggestion);
                    Log.e("Error Message", "Message : " + e.getMessage());
                    mTextUpdateListener.ErrorOccurred();
                }
            }
        };
    }

    /*
    *  開始監聽並轉換為文字
    *  RecognitionType為DICTATION(聽寫)
    *  Detection為None(自行開始與結束)
    *  Language由外部傳入(繁體中文代碼為"cmn-TWN")
     */
    public void recognizeStart() {
        options.setRecognitionType(RecognitionType.DICTATION);
        options.setDetection(DetectionType.None);
        options.setLanguage(new Language(CHINESE_CODE));

        transaction = session.recognize(options, SpeechListener());
    }

    public void recognizeStop() {
        transaction.stopRecording();
    }

    public void recognizeForceStop(boolean recognizeStatus) {
        forceStop = true;
        if (recognizeStatus) {
            transaction.stopRecording();
        }
        if (recorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            recorder.stop();
        }
        mHandlerTime.removeCallbacks(recordRun);
        recorder.release();
    }

    public void setTextUpdateListener(onTextUpdateListener listener) {
        this.mTextUpdateListener = listener;
    }

    public void startCaculateDB() {
        bufferSize = AudioRecord.getMinBufferSize(
                RECORDER_SAMPLERATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING
        );
        recorder = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING,
                bufferSize
        );
        //initial and start AudioRecord
        recorder.startRecording();

        //new a thread to run and log volume
        mHandlerTime.post(recordRun);
    }

    private final Runnable recordRun = new Runnable() {
        @Override
        public void run() {
            short data[] = new short[bufferSize];
            long read = recorder.read(data, 0, bufferSize);

            long v = 0;
            for (int i = 0; i < data.length; i++) {
                v += data[i] * data[i];
            }
            double mean = v / (double) read;
            double volume = 10 * Math.log10(mean);
            Log.e("db", "分貝值:" + volume);

            if (volume > 60) {
                mTextUpdateListener.UserTalking();
                recorder.stop();
                recorder.release();
            } else {
                mHandlerTime.postDelayed(this, 200);
            }
        }
    };
}
