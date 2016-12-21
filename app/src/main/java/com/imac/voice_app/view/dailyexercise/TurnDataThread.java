package com.imac.voice_app.view.dailyexercise;

import android.app.Activity;
import android.os.Handler;

/**
 * Created by isa on 2016/9/26.
 */
public class TurnDataThread implements Runnable {
    private Handler handler;
    private Activity activity;
    private int witchPictureIndex = 0;
    private DataChangeEvent event;
    private int recycle;
    public TurnDataThread(Activity activity,int recycle) {
        this.activity = activity;
        handler = new Handler();
        this.recycle=recycle;
    }

    @Override
    public void run() {
        witchPictureIndex = witchPictureIndex % recycle;
        event.onDataChangeEvent(witchPictureIndex);
        witchPictureIndex++;
        handler.postDelayed(this, 1000*10);
    }

    public interface DataChangeEvent {
        void onDataChangeEvent(int witchData);
    }

    public void setDataChangeEvent(DataChangeEvent event) {
        this.event = event;
    }

    public void start() {
        run();
    }
}
