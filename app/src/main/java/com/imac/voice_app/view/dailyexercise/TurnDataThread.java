package com.imac.voice_app.view.dailyexercise;

import android.app.Activity;
import android.os.Handler;

/**
 * Created by isa on 2016/9/26.
 */
public class TurnDataThread extends Thread {
    private Activity activity;
    private int witchPictureIndex = 0;
    private DataChangeEvent event;
    private int recycle;
    private boolean isPause = false;
    private boolean isFinish = false;
    private int countScend = 0;
    private Object mObject;
    private Handler mHandler;

    public TurnDataThread(Activity activity, int recycle) {
        this.activity = activity;
        this.recycle = recycle;
        mObject = new Object();
        mHandler = new Handler();
    }

    @Override
    public void run() {
        if (!isFinish) {
            if (!isPause) {
                witchPictureIndex = witchPictureIndex % recycle;
                if (countScend % 10 == 0) {
                    event.onDataChangeEvent(witchPictureIndex);
                    witchPictureIndex++;
                }
                countScend++;
                mHandler.postDelayed(this, 1000);
            }
        }
    }

    public interface DataChangeEvent {
        void onDataChangeEvent(int witchData);
    }

    public void setDataChangeEvent(DataChangeEvent event) {
        this.event = event;
    }

    public void pause() {
        isPause = true;
        mHandler.removeCallbacks(this);
    }

    public void restart() {
        isPause = false;
        mHandler.postDelayed(this, 1000);
    }

    public void finish() {
        isFinish = true;
        interrupt();
    }
}
