package com.imac.dr.voice_app.module;

import android.os.Handler;

/**
 * module to count second
 * Created by flowmaHuang on 2016/9/19.
 */
public class CountSecond {
    private enum CountStatus {
        STARTING_STOP_WATCH, STARTING_COUNT_DOWN, STOP, PAUSE
    }
    private CountStatus countStatus;
    private CountStatus tempStatus;

    private Handler handler;
    private int countDownSec;
    private int arrivalSec;

    public interface countSecondCallBack {
        void countPerSecond(int sec);

        void finishCount();
    }

    private countSecondCallBack callBack = null;

    public CountSecond(countSecondCallBack callBack) {
        countStatus = CountStatus.STOP;
        this.callBack = callBack;
        handler = new Handler();
    }

    private void initTimerTask(int finishSecond) {
        arrivalSec = finishSecond;
        countDownSec = finishSecond;

        handler.postDelayed(countSec, 1000);
    }

    public void startCountWithStopWatch(int finishSecond) {
        if (countStatus == CountStatus.STOP) {
            countStatus = CountStatus.STARTING_STOP_WATCH;
            callBack.countPerSecond(0);
            initTimerTask(finishSecond);
        }
    }

    public void startCountWithCountDown(int finishSecond) {
        if (countStatus == CountStatus.STOP) {
            countStatus = CountStatus.STARTING_COUNT_DOWN;
            callBack.countPerSecond(finishSecond);
            initTimerTask(finishSecond);
        }
    }

    public void stopCount() {
        if (countStatus == CountStatus.PAUSE) {
            countStatus = CountStatus.STOP;
            countDownSec = 0;
            arrivalSec = 0;
        } else if (countStatus != CountStatus.STOP) {
            handler.removeCallbacks(countSec);
            countStatus = CountStatus.STOP;
            countDownSec = 0;
            arrivalSec = 0;
        }
    }

    public void pauseCount() {
        if (countStatus == CountStatus.STARTING_COUNT_DOWN || countStatus == CountStatus.STARTING_STOP_WATCH) {
            tempStatus = countStatus;
            countStatus = CountStatus.PAUSE;
            handler.removeCallbacks(countSec);
        }
    }

    public void continueCount() {
        if (countDownSec != 0 && countStatus == CountStatus.PAUSE) {
            countStatus = tempStatus;
            handler.postDelayed(countSec, 1000);
        }
    }

    private Runnable countSec = new Runnable() {
        @Override
        public void run() {
            if (countStatus == CountStatus.STARTING_STOP_WATCH) {
                countDownSec--;
                callBack.countPerSecond(arrivalSec - countDownSec);
            } else if (countStatus == CountStatus.STARTING_COUNT_DOWN) {
                countDownSec--;
                callBack.countPerSecond(countDownSec);
            }

            if (countDownSec == 0) {
                callBack.finishCount();
                countStatus = CountStatus.STOP;
                countDownSec = 0;
                arrivalSec = 0;
                handler.removeCallbacks(this);
            } else {
                handler.postDelayed(this, 1000);
            }
        }
    };
}
