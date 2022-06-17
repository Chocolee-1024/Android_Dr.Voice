package com.imac.dr.voice_app.view.dailyexercise;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

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
        this.recycle = recycle; //12,4
        mHandler = new Handler();
    }

    @Override
    public void run() {
        if (!isFinish) {
            if (!isPause) {
                witchPictureIndex = witchPictureIndex % recycle;
                Log.d("note", "witchPictureIndex=目前第幾個 : "+witchPictureIndex);
                Log.d("note", "recycle=有幾個子項: "+recycle);
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

                //note 這個tread每一秒run一次
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
