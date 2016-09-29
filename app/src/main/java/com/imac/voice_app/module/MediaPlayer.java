package com.imac.voice_app.module;

import android.app.Activity;

/**
 * Created by isa on 2016/9/26.
 */
public class MediaPlayer {
    private android.media.MediaPlayer player;
    private Activity activity;

    public enum Status {
        PLAY,
        PAUSE,
        STOP
    }

    private Status status;

    public MediaPlayer(Activity activity, int id) {
        this.activity = activity;
        player = android.media.MediaPlayer.create(activity, id);
    }

    public void startPlay() {
        player.start();
        status = Status.PLAY;
    }

    public void pausePlay() {
        player.pause();
        status = Status.PAUSE;
    }

    public void stopPlay() {
        player.stop();
        status = Status.STOP;
    }

    public Status getStatus() {
        return status;
    }
}
