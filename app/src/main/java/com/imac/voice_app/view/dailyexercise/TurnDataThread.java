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
    private Handler mHandler;
    private int delaySec = 2;
    private boolean isFirst = true;

    public TurnDataThread(Activity activity, int recycle) {
        this.activity = activity;
        this.recycle = recycle;
        mHandler = new Handler();
    }

    @Override
    public void run() {
        if (!isFinish) {
            if (!isPause) {
                witchPictureIndex = witchPictureIndex % recycle;
                if (isFirst) {
                    event.onDataChangeEvent(witchPictureIndex);
                    witchPictureIndex++;
                    isFirst = false;
                } else if (countScend == delaySec) {
                    event.onDataChangeEvent(witchPictureIndex);
                    witchPictureIndex++;
                    countScend = 0;
                }
                countScend++;

                mHandler.postDelayed(this, 1000);
            }
        }
    }

    public void setDelaySec(int delaySec) {
        this.delaySec = delaySec;
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
