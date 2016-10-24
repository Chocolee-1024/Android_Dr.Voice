package com.imac.voice_app.view.dailyexercise;

import android.app.Activity;
import android.os.Handler;

/**
 * Created by isa on 2016/9/26.
 */
public class TurnPictureThread implements Runnable {
    private Handler handler;
    private Activity activity;
    private int witchPictureIndex = 0;
    private ImageChangeEvent event;

    public TurnPictureThread(Activity activity) {
        this.activity = activity;
        handler = new Handler();
    }

    @Override
    public void run() {
        witchPictureIndex = witchPictureIndex % 4;
        event.onImageChangeEvent(witchPictureIndex);
        witchPictureIndex++;
        handler.postDelayed(this, 2000);
    }

    public interface ImageChangeEvent {
        void onImageChangeEvent(int witchPicture);
    }

    public void setImageChangeEvent(ImageChangeEvent event) {
        this.event = event;
    }

    public void start() {
        run();
    }
}
